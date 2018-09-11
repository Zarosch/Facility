package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.fly")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player player = (Player) cs;
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FLY_SELF_OFF.getLocal());
                } else {
                    player.setAllowFlight(true);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FLY_SELF_ON.getLocal());
                }
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
        } else {
            if (!cs.hasPermission("facility.command.fly.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target.getAllowFlight()) {
                target.setAllowFlight(false);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FLY_OTHER_OFF.getLocal().replaceAll("%player", target.getName()));
            } else {
                target.setAllowFlight(true);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FLY_OTHER_ON.getLocal().replaceAll("%player", target.getName()));
            }
        }
        return true;
    }

}
