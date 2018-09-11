package me.velz.facility.commands;

import me.velz.facility.utils.ItemBuilder;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkullCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.skull")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/skull <Owner>"));
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }
        Player player = (Player) cs;
        player.getInventory().addItem(new ItemBuilder().setMaterial(Material.SKULL_ITEM).setDurability((short) 3).setOwner(args[0]).build());
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ITEMS_SKULL.getLocal().replaceAll("%skull", args[0]));
        return true;
    }

}
