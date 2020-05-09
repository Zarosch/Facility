package me.velz.facility.commands;

import lombok.Getter;
import lombok.Setter;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlobalMuteCommand implements CommandExecutor {

    @Getter @Setter
    private static boolean globalMute = false;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.globalmute")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (isGlobalMute()) {
            Bukkit.broadcastMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_GLOBALMUTE_OFF.getLocal().replaceAll("%player", cs.getName()));
            setGlobalMute(false);
        } else {
            Bukkit.broadcastMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_GLOBALMUTE_ON.getLocal().replaceAll("%player", cs.getName()));
            setGlobalMute(true);
        }
        return true;
    }

}
