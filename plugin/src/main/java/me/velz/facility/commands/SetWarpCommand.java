package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabaseWarp;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    private final Facility plugin;

    public SetWarpCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.setwarp")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length != 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/setwarp <Warp>"));
            return true;
        }
        if (plugin.getWarps().containsKey(args[0])) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_FOUND.getLocal());
            return true;
        }
        if (cs instanceof Player) {
            Player player = (Player) cs;
            DatabaseWarp warp = new DatabaseWarp(args[0], player.getLocation());
            Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                plugin.getDatabase().addWarp(args[0], player.getLocation());
                plugin.getWarps().put(args[0], warp);
            });
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_SET.getLocal().replaceAll("%warp", args[0]));
        } else {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }
        return true;
    }

}
