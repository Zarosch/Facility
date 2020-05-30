package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarpCommand implements CommandExecutor {

    private final Facility plugin;

    public DelWarpCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.delwarp")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length != 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/delwarp <warp>"));
            return true;
        }
        if (!plugin.getWarps().containsKey(args[0])) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOTFOUND.getLocal());
            return true;
        }
        if (cs instanceof Player) {
            Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                plugin.getDatabase().deleteWarp(args[0]);
                plugin.getWarps().remove(args[0]);
            });
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_DEL.getLocal().replaceAll("%warp", args[0]));
        } else {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }
        return true;
    }

}
