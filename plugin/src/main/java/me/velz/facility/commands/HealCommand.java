package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.heal")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player player = (Player) cs;
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_HEAL_SELF.getLocal());
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
        } else {
            if (!cs.hasPermission("facility.command.heal.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            target.setHealth(target.getMaxHealth());
            target.setFoodLevel(20);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_HEAL_OTHER.getLocal().replaceAll("%player", target.getName()));
        }
        return true;
    }

}
