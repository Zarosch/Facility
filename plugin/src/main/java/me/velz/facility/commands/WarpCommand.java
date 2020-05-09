package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    
    private final Facility plugin;

    public WarpCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.warp")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 1) {
            if (!plugin.getWarps().containsKey(args[0])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOTFOUND.getLocal());
                return true;
            }
            if (cs instanceof Player) {
                Player player = (Player) cs;
                if (player.hasPermission("facility.bypass.teleportdelay")) {
                    player.teleport(plugin.getWarps().get(args[0]).getLoc());
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_TELEPORT_SELF.getLocal().replaceAll("%warp", args[0]));
                } else {
                    new FacilityTeleport(player, plugin.getWarps().get(args[0]).getLoc(), MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_TELEPORT_SELF.getLocal().replaceAll("%warp", args[0]), plugin.getFileManager().getTeleportDelay());
                }
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            return true;
        }
        if (args.length == 2) {
            if (!cs.hasPermission("facility.command.warp.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            if (!plugin.getWarps().containsKey(args[0])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            target.teleport(plugin.getWarps().get(args[0]).getLoc());
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_TELEPORT_OTHER.getLocal().replaceAll("%player", target.getName()).replaceAll("%warp", args[0]));
            return true;
        }
        Bukkit.dispatchCommand((Player)cs, "warplist");
        return true;
    }

}
