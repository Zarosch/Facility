package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.broadcast")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/broadcast [Nachricht]"));
            return true;
        }
        String message = "";
        for (String msg : args) {
            message = message + " " + msg;
        }
        message = message.substring(1, message.length());
        Bukkit.broadcastMessage(MessageUtil.CHAT_BROADCAST.getLocal().replaceAll("%message", ChatColor.translateAlternateColorCodes('&', message)));
        return true;
    }

}
