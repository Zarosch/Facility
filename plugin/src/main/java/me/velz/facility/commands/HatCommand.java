package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HatCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String args, String[] label) {
        if (!cs.hasPermission("facility.command.hat")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }
        Player player = (Player) cs;
        if (Facility.getInstance().getVersion().getItemInMainHand(player) == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOITEMINHAND.getLocal());
            return true;
        }
        ItemStack helmet = null;
        if (player.getInventory().getHelmet() != null) {
            helmet = player.getInventory().getHelmet().clone();
        }
        player.getInventory().setHelmet(Facility.getInstance().getVersion().getItemInMainHand(player));
        if (helmet != null) {
            Facility.getInstance().getVersion().setItemInMainHand(player, helmet);
        } else {
            Facility.getInstance().getVersion().setItemInMainHand(player, null);
        }
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_HAT.getLocal());
        return true;
    }

}
