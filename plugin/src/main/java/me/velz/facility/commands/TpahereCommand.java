package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityTpa;
import me.velz.facility.utils.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpahereCommand implements CommandExecutor {

    private final Facility plugin;

    public TpahereCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.tpahere")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tpahere <player>"));
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
            if (TpaCommand.getTpaStorage().containsKey(player)) {
                FacilityTpa facilityTpa = TpaCommand.getTpaStorage().get(player);
                if (facilityTpa.getPlayer2() == target) {
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ALREADY.getLocal());
                    return true;
                }
                facilityTpa.getPlayer1().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_CANCEL_PLAYER.getLocal());
                facilityTpa.getPlayer2().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_CANCEL_TARGET.getLocal());
                TpaCommand.getTpaStorage().remove(player);
            }
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_PLAYER.getLocal().replaceAll("%player", target.getName()));
            target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_TPAHERETARGET.getLocal().replaceAll("%player", player.getName()));
            TextComponent accept = new TextComponent(MessageUtil.TELEPORT_TPA_ACCEPT.getLocal());
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + player.getName()));
            TextComponent deny = new TextComponent(MessageUtil.TELEPORT_TPA_DENY.getLocal());
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + player.getName()));
            accept.addExtra(deny);
            target.spigot().sendMessage(accept);
            TpaCommand.getTpaStorage().put(player, new FacilityTpa(player, target, "tpahere", 60));
        }
        return true;
    }

}
