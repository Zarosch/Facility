package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MotdCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.motd")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_MOTD.getLocal().replaceAll("%player", cs.getName()));
        return true;
    }

}
