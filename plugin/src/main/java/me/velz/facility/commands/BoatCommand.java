package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class BoatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.boat")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        Player player = (Player)cs;
        Boat boat = (Boat)player.getWorld().spawnEntity(player.getLocation(), EntityType.BOAT);
        boat.addPassenger(player);
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_BOAT.getLocal());
        return true;
    }
}
