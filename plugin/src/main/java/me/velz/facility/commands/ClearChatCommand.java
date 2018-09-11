package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.clearchat")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        Bukkit.broadcastMessage(StringUtils.repeat(" \n", 120));
        Bukkit.broadcastMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_CLEAR.getLocal().replaceAll("%player", cs.getName()));
        return true;
    }

}
