package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.objects.FacilityTpa;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpacceptCommand implements CommandExecutor {
    
    private final Facility plugin;

    public TpacceptCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.tpaccept")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tpaccept <Spieler>"));
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
            if (!TpaCommand.getTpaStorage().containsKey(target)) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_NOREQUEST.getLocal());
                return true;
            }
            FacilityTpa facilityTpa = TpaCommand.getTpaStorage().get(target);
            if (facilityTpa.getPlayer2() != player) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_NOREQUEST.getLocal());
                return true;
            }
            if (facilityTpa.getType().equalsIgnoreCase("tpa")) {
                if (target.hasPermission("facility.bypass.teleportdelay")) {
                    target.teleport(player);
                    target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_PLAYER.getLocal().replaceAll("%player", player.getName()));
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_TARGET.getLocal().replaceAll("%player", target.getName()));
                } else {
                    new FacilityTeleport(target, player, MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_PLAYER.getLocal().replaceAll("%player", player.getName()), plugin.getFileManager().getTeleportDelay());
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_TARGET.getLocal().replaceAll("%player", target.getName()));
                }
            }
            if (facilityTpa.getType().equalsIgnoreCase("tpahere")) {
                if (player.hasPermission("facility.bypass.teleportdelay")) {
                    player.teleport(target);
                    target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_PLAYER.getLocal().replaceAll("%player", player.getName()));
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_TARGET.getLocal().replaceAll("%player", target.getName()));
                } else {
                    target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_PLAYER.getLocal().replaceAll("%player", player.getName()));
                    new FacilityTeleport(player, target, MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_ACCPTED_TARGET.getLocal().replaceAll("%player", target.getName()), plugin.getFileManager().getTeleportDelay());
                }
            }
            TpaCommand.getTpaStorage().remove(target);
        }
        return true;
    }

}
