package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearChatCommand implements CommandExecutor {

    private final Facility plugin;

    public ClearChatCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.clearchat")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        for(int i = 0; i != 120; i++) {
            Bukkit.broadcastMessage(" ");
        }
        Bukkit.broadcastMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_CLEAR.getLocal().replaceAll("%player", cs.getName()));
        return true;
    }

}
