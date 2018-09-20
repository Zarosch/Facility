package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.ping")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player player = (Player) cs;
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_PING_SELF.getLocal().replaceAll("%ping", String.valueOf(getPing(player))));
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
        } else {
            if (!cs.hasPermission("facility.command.ping.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_PING_OTHER.getLocal().replaceAll("%ping", String.valueOf(getPing(target))).replaceAll("%player", target.getName()));
        }
        return true;
    }

    private int getPing(Player player) {
        return Facility.getInstance().getVersion().getPing(player);
    }
}
