package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
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
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.home")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }

        final Player player = (Player) cs;
        final String uuid;
        final String home;
        if (args.length >= 1) {
            home = args[0];
        } else {
            home = "home";
        }
        if(args.length >= 2) {
            if(!player.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.home.other")) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            uuid = plugin.getDatabase().getUUID(args[1]);
            if(uuid == null) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
        } else {
            uuid = player.getUniqueId().toString();
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(uuid);
            if (!dbPlayer.getHomes().containsKey(home)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_NOTFOUND.getLocal());
            } else {
                final Location loc = dbPlayer.getHomes().get(home);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (player.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".bypass.teleportdelay")) {
                        player.teleport(loc);
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_TELEPORT.getLocal().replaceAll("%home", home));
                    } else {
                        new FacilityTeleport(player, loc, MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_TELEPORT.getLocal().replaceAll("%home", home), plugin.getFileManager().getTeleportDelay());
                    }
                });
            }
        });
        return true;
    }

}
