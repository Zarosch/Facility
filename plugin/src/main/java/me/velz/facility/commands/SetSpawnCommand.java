package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final Facility plugin;

    public SetSpawnCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.setspawn")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }
        Player player = (Player) cs;
        plugin.getFileManager().setSpawnLocation(player.getLocation());
        plugin.getFileManager().getSpawn().set("spawn", player.getLocation());
        plugin.getFileManager().getSpawn().save();
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.SPAWN_SET.getLocal());
        return true;
    }

}
