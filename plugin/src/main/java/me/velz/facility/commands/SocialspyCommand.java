package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SocialspyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.socialspy")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        final Player player = (Player) cs;
        if (MsgCommand.getSOCIALSPY().contains(player)) {
            MsgCommand.getSOCIALSPY().remove(player);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_GLOBALMUTE_OFF.getLocal());
        } else {
            MsgCommand.getSOCIALSPY().add(player);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_SOCIALSPY_ON.getLocal());
        }
        return true;
    }

}
