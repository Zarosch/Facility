package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.feed")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (cs instanceof Player) {
                final Player player = (Player) cs;
                player.setFoodLevel(20);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FEED_SELF.getLocal());
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
        } else {
            if (!cs.hasPermission("facility.command.feed.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            final Player target = Bukkit.getPlayer(args[0]);
            target.setFoodLevel(20);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FEED_OTHER.getLocal().replaceAll("%player", target.getName()));
        }
        return true;
    }

}
