package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearinvCommand implements CommandExecutor {

    private final Facility plugin;

    public ClearinvCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.clearinv")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            final Player player = (Player) cs;
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_CLEARINV_SELF.getLocal());
        } else {
            if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.clearinv.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            final Player target = Bukkit.getPlayer(args[0]);
            target.getInventory().clear();
            target.getInventory().setArmorContents(null);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_CLEARINV_OTHER.getLocal().replaceAll("%player", target.getName()));
        }
        return true;
    }

}
