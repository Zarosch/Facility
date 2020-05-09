package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    
    private final Facility plugin;

    public SpawnCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.spawn")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            Player player = (Player) cs;
            if (plugin.getFileManager().getSpawnLocation() == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.SPAWN_NOTSET.getLocal());
                return true;
            }
            if (player.hasPermission("facility.bypass.teleportdelay")) {
                player.teleport(plugin.getFileManager().getSpawnLocation());
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.SPAWN_TELEPORT_SELF.getLocal());
            } else {
                new FacilityTeleport(player, plugin.getFileManager().getSpawnLocation(), MessageUtil.PREFIX.getLocal() + MessageUtil.SPAWN_TELEPORT_SELF.getLocal(), plugin.getFileManager().getTeleportDelay());
            }
        } else {
            if (!cs.hasPermission("facility.command.spawn.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (plugin.getFileManager().getSpawnLocation() == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.SPAWN_NOTSET.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            target.teleport(plugin.getFileManager().getSpawnLocation());
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.SPAWN_TELEPORT_OTHER.getLocal().replaceAll("%player", target.getName()));
        }
        return true;
    }

}
