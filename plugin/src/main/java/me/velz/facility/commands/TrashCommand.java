package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TrashCommand implements CommandExecutor {

    private final Facility plugin;

    public TrashCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.trash")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        Inventory inventory = Bukkit.createInventory(null, 9*3, MessageUtil.TRASH_TITLE.getLocal());
        Player player = (Player)cs;
        player.openInventory(inventory);
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TRASH_MESSAGE.getLocal());
        return true;
    }
    
    
    
}
