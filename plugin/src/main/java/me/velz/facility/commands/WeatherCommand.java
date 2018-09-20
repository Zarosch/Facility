package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WeatherCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        //<editor-fold defaultstate="collapsed" desc="/weather">
        if (cmd.getName().equalsIgnoreCase("weather")) {
            if (!cs.hasPermission("facility.command.weather")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }

            Player player = (Player) cs;
            if (args.length == 1) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/weather <rain/sun>"));
                return true;
            }

            if (args[0].equalsIgnoreCase("rain")) {
                player.getWorld().setStorm(true);
                player.getWorld().setWeatherDuration(1000);
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_WEATHER_RAIN.getLocal());
                return true;
            }

            if (args[0].equalsIgnoreCase("sun")) {
                player.getWorld().setStorm(false);
                player.getWorld().setThundering(false);
                player.getWorld().setWeatherDuration(0);
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_WEATHER_RAIN.getLocal());
                return true;
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/weather <rain/sun>"));
            return true;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="/sun">
        if (cmd.getName().equalsIgnoreCase("sun")) {
            if (!cs.hasPermission("facility.command.weather") && !cs.hasPermission("facility.command.sun")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }

            Player player = (Player) cs;
            player.getWorld().setWeatherDuration(0);
            player.getWorld().setStorm(false);
            player.getWorld().setThundering(false);
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_WEATHER_SUN.getLocal());
            return true;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="/rain">
        if (cmd.getName().equalsIgnoreCase("rain")) {
            if (!cs.hasPermission("facility.command.weather") && !cs.hasPermission("facility.command.rain")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }

            Player player = (Player) cs;
            player.getWorld().setStorm(true);
            player.getWorld().setWeatherDuration(1000);
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_WEATHER_RAIN.getLocal());
            return true;
        }
        //</editor-fold>

        return true;
    }

}
