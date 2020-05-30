package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class FreezeCommand implements CommandExecutor {

    private final Facility plugin;

    public FreezeCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.freeze")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/freeze <player>"));
            return true;
        } else {
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            final Player target = Bukkit.getPlayer(args[0]);
            if (target.hasMetadata("freeze")) {
                target.removeMetadata("freeze", Facility.getInstance());
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FREEZE_OFF.getLocal().replaceAll("%player", target.getName()));
            } else {
                target.setMetadata("freeze", new FixedMetadataValue(Facility.getInstance(), true));
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_FREEZE_ON.getLocal().replaceAll("%player", target.getName()));
            }
        }
        return true;
    }

}
