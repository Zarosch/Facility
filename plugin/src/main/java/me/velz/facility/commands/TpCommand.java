package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpCommand implements CommandExecutor {

    private final Facility plugin;

    public TpCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.tp")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tp <Spieler> [Spieler]"));
            return true;
        }
        if (args.length == 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = (Player) cs;
            Player target = Bukkit.getPlayer(args[0]);
            player.teleport(target);
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TP_SELF.getLocal().replaceAll("%player", target.getName()));
        }
        if (args.length == 2) {
            if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.tp.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = Bukkit.getPlayer(args[0]);
            Player target = Bukkit.getPlayer(args[1]);
            player.teleport(target);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TP_OTHER.getLocal().replaceAll("%player", player.getName()).replaceAll("%target", target.getName()));
        }
        if (args.length == 3) {
            try {
                Integer x = Integer.valueOf(args[0]);
                Integer y = Integer.valueOf(args[1]);
                Integer z = Integer.valueOf(args[2]);
                Player player = (Player) cs;
                Location loc = new Location(player.getWorld(), x, y, z);
                player.teleport(loc);
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPLOC_SELF.getLocal().replaceAll("%loc", x + "," + y + "," + z));
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
        }
        if (args.length == 4) {
            try {
                Integer x = Integer.valueOf(args[0]);
                Integer y = Integer.valueOf(args[1]);
                Integer z = Integer.valueOf(args[2]);
                Player player = (Player) cs;
                Location loc = new Location(player.getWorld(), x, y, z);
                if(Bukkit.getPlayer(args[3]) == null) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    return true;
                }
                Player target = Bukkit.getPlayer(args[3]);
                target.teleport(loc);
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPLOC_OTHER.getLocal().replaceAll("%player", target.getName()).replaceAll("%loc", x + "," + y + "," + z));
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
        }
        return true;
    }

}
