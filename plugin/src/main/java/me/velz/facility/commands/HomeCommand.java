package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    
    private final Facility plugin;

    public HomeCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.home")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }

        final Player player = (Player) cs;
        final String home;
        if (args.length == 1) {
            home = args[0];
        } else {
            home = "home";
        }
        final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(player.getUniqueId().toString());
        if (!dbPlayer.getHomes().containsKey(home)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_NOTFOUND.getLocal());
            return true;
        }
        final Location loc = dbPlayer.getHomes().get(home);
        if (player.hasPermission("facility.bypass.teleportdelay")) {
            player.teleport(loc);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_TELEPORT.getLocal().replaceAll("%home", home));
        } else {
            new FacilityTeleport(player, loc, MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_TELEPORT.getLocal().replaceAll("%home", home), plugin.getFileManager().getTeleportDelay());
        }
        return true;
    }

}
