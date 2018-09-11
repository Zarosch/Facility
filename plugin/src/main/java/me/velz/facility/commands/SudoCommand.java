package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.sudo")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            return true;
        }
        if (args.length <= 2) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/sudo <Player> <Command/c:Nachricht>"));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        String message = "";
        int arg = 0;
        for (String msg : args) {
            if (arg != 0) {
                message = message + " " + msg;
            }
            arg++;
        }
        message = message.substring(1, message.length());
        if (message.startsWith("c:")) {
            target.chat(message.substring(2));
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_SUDO_CHAT.getLocal());
        } else {
            Bukkit.dispatchCommand(target, message);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_SUDO_COMMAND.getLocal());
        }
        return true;
    }

}
