package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoreCommand implements CommandExecutor {

    private final Facility plugin;

    public MoreCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.more")) {
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
        ItemStack is = Facility.getInstance().getVersion().getItemInMainHand(player);
        is.setAmount(64);
        Facility.getInstance().getVersion().setItemInMainHand(player, is);
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ITEMS_MORE.getLocal());
        return true;
    }

}
