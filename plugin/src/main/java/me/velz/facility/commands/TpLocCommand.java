package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpLocCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.tploc")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length != 3 && args.length != 4) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tploc <X> <Y> <Z> [Player]"));
            return true;
        }
        try {
            Integer x = Integer.valueOf(args[0]);
            Integer y = Integer.valueOf(args[1]);
            Integer z = Integer.valueOf(args[2]);
            Player player = (Player) cs;
            Location loc = new Location(player.getWorld(), x, y, z);
            if (args.length == 3) {
                player.teleport(loc);
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPLOC_SELF.getLocal().replaceAll("%loc", x + "," + y + "," + z));
                return true;
            }
            if (args.length == 4) {
                if (Bukkit.getPlayer(args[3]) == null) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    return true;
                }
                Player target = Bukkit.getPlayer(args[3]);
                target.teleport(loc);
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPLOC_OTHER.getLocal().replaceAll("%player", target.getName()).replaceAll("%loc", x + "," + y + "," + z));
                return true;
            }
        } catch (NumberFormatException ex) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            return true;
        }
        return true;
    }

}
