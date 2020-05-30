package me.velz.facility.commands;

import java.util.HashMap;
import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.objects.FacilityTpa;
import me.velz.facility.utils.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final Facility plugin;

    public TpaCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Getter
    private static final HashMap<Player, FacilityTeleport> teleportStorage = new HashMap();

    @Getter
    private static final HashMap<Player, FacilityTpa> tpaStorage = new HashMap();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.tpa")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tpa <player>"));
            return true;
        }
        if (args.length == 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = (Player) cs;
            Player target = Bukkit.getPlayer(args[0]);
            if (player == target) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_SAMEPLAYER.getLocal());
                return true;
            }
            if (this.getTpaStorage().containsKey(player)) {
                FacilityTpa facilityTpa = this.getTpaStorage().get(player);
                if (facilityTpa.getPlayer2() == target) {
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ALREADY.getLocal());
                    return true;
                }
                facilityTpa.getPlayer1().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_CANCEL_PLAYER.getLocal().replaceAll("%player", target.getName()));
                facilityTpa.getPlayer2().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_CANCEL_TARGET.getLocal().replaceAll("%player", player.getName()));
                this.getTpaStorage().remove(player);
            }
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_PLAYER.getLocal().replaceAll("%player", target.getName()));
            target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_TARGET.getLocal().replaceAll("%player", player.getName()));
            TextComponent accept = new TextComponent(MessageUtil.TELEPORT_TPA_ACCEPT.getLocal());
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + player.getName()));
            TextComponent deny = new TextComponent(MessageUtil.TELEPORT_TPA_DENY.getLocal());
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + player.getName()));
            accept.addExtra(deny);
            target.spigot().sendMessage(accept);
            target.playSound(target.getLocation(), Facility.getInstance().getVersion().getSound("BLOCK_NOTE_PLING"), 1, 1);
            this.getTpaStorage().put(player, new FacilityTpa(player, target, "tpa", 60));
        }
        return true;
    }

    public static void schedule() {
        if (!TpaCommand.getTpaStorage().isEmpty()) {
            TpaCommand.getTpaStorage().values().forEach((facilityTpa) -> {
                if (facilityTpa.getTimer() == 0) {
                    facilityTpa.getPlayer1().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_TIMEOUT_PLAYER.getLocal().replaceAll("%player", facilityTpa.getPlayer2().getName()));
                    facilityTpa.getPlayer2().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_TIMEOUT_TARGET.getLocal().replaceAll("%player", facilityTpa.getPlayer1().getName()));
                    TpaCommand.getTpaStorage().remove(facilityTpa.getPlayer1());
                } else {
                    facilityTpa.setTimer(facilityTpa.getTimer() - 1);
                }
            });
        }
        if (!TpaCommand.getTeleportStorage().isEmpty()) {
            TpaCommand.getTeleportStorage().values().forEach((facilityTeleport) -> {
                if (facilityTeleport.seconds == 0) {
                    facilityTeleport.teleport();
                    TpaCommand.getTeleportStorage().remove(facilityTeleport.player);
                } else {
                    facilityTeleport.seconds--;
                }
            });
        }
    }

}
