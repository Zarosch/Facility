package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JumpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.jump")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        Player player = (Player) cs;
        Location loc = Facility.getInstance().getVersion().getTargetBlock(player, 32);
        if (loc == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_JUMP_TOOFAR.getLocal());
            return true;
        }
        loc.setY(loc.getY() + 1);
        player.teleport(loc);
        return true;
    }

}
