package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamchatCommand implements CommandExecutor {
    
    private final Facility plugin;

    public TeamchatCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.teamchat")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/teamchat [message]"));
            return true;
        } else {
            String message = "";
            for (String msg : args) {
                message = message + " " + msg;
            }
            message = message.substring(1, message.length());
            String prefix = "";
            if(plugin.getImplementations().getVault() != null) {
                prefix = plugin.getImplementations().getVault().getChat().getPlayerPrefix((Player)cs);
            }
            
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.teamchat")) {
                    player.sendMessage(MessageUtil.CHAT_TEAMCHAT.getLocal().replaceAll("%player", ChatColor.translateAlternateColorCodes('&', prefix) + cs.getName()).replaceAll("%message", message));
                }
            }
        }
        return true;
    }

}
