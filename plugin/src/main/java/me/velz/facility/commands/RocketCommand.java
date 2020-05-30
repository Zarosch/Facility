package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RocketCommand implements CommandExecutor {

    private final Facility plugin;

    public RocketCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.rocket")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            final Player player = (Player) cs;
            player.setVelocity(new Vector(1, 64, 0));
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_ROCKET_SELF.getLocal());
        } else {
            if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.rocket.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            final Player target = Bukkit.getPlayer(args[0]);
            target.setVelocity(new Vector(1, 64, 0));
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_ROCKET_OTHER.getLocal().replaceAll("%player", target.getName()));
        }
        return true;
    }

}
