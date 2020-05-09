package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoatCommand implements CommandExecutor {

    private final Facility plugin;

    public BoatCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.boat")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        Player player = (Player)cs;
        plugin.getVersion().addPlayerToBoat(player);
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_BOAT.getLocal());
        return true;
    }
}
