package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        
        //<editor-fold defaultstate="collapsed" desc="/time">
        if (cmd.getName().equalsIgnoreCase("time")) {
            if (!cs.hasPermission("facility.command.time")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }

            if (args.length == 0) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/time <day/night/<number>>"));
                return true;
            }
            Player player = (Player) cs;
            if (args[0].equalsIgnoreCase("day")) {
                player.getWorld().setTime(0700);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_TIME_DAY.getLocal());
                return true;
            }
            if (args[0].equalsIgnoreCase("night")) {
                player.getWorld().setTime(2200);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_TIME_NIGHT.getLocal());
                return true;
            }
            try {
                Integer number = Integer.valueOf(args[0]);
                player.getWorld().setTime(number);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_TIME_NUMBER.getLocal().replaceAll("%number", String.valueOf(number)));
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/time <day/night/<number>>"));
            }
            return true;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="/day">
        if (cmd.getName().equalsIgnoreCase("day")) {
            if (!cs.hasPermission("facility.command.day") && !cs.hasPermission("facility.command.time")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }

            Player player = (Player) cs;
            player.getWorld().setTime(0700);
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_TIME_DAY.getLocal());
            return false;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="/night">
        if (cmd.getName().equalsIgnoreCase("night")) {
            if (!cs.hasPermission("facility.command.night")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }

            Player player = (Player) cs;
            player.getWorld().setTime(2200);
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_TIME_NIGHT.getLocal());
        }
        //</editor-fold>
        
        return true;
    }
}
