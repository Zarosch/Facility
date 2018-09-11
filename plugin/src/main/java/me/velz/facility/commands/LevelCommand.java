package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.level")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0 || args.length == 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/level <set/take/add/check> [Player] [Level]"));
            return true;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("check")) {
                Player player = Bukkit.getPlayer(args[1]);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_LEVEL_GET.getLocal().replaceAll("%player", player.getName()).replaceAll("%level", String.valueOf(player.getLevel())));
                return true;
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                Player player = Bukkit.getPlayer(args[1]);
                try {
                    Integer level = Integer.valueOf(args[2]);
                    player.setLevel(level);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_LEVEL_SET.getLocal().replaceAll("%level", String.valueOf(level)).replaceAll("%player", player.getName()));
                } catch (NumberFormatException ex) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("take")) {
                Player player = Bukkit.getPlayer(args[1]);
                try {
                    Integer level = Integer.valueOf(args[2]);
                    player.setLevel(player.getLevel() - level);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_LEVEL_TAKE.getLocal().replaceAll("%player", player.getName()).replaceAll("%level", String.valueOf(level)));
                } catch (NumberFormatException ex) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                Player player = Bukkit.getPlayer(args[1]);
                try {
                    Integer level = Integer.valueOf(args[2]);
                    player.setLevel(player.getLevel() + level);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_LEVEL_ADD.getLocal().replaceAll("%player", player.getName()).replaceAll("%level", String.valueOf(level)));
                } catch (NumberFormatException ex) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                }
                return true;
            }
        }
        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/level <set/take/add/check> [Player] [Level]"));
        return true;
    }

}
