package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class GodmodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.godmode")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player player = (Player) cs;
                if (player.hasMetadata("godmode")) {
                    player.removeMetadata("godmode", Facility.getInstance());
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GODMODE_SELF_OFF.getLocal());
                } else {
                    player.setMetadata("godmode", new FixedMetadataValue(Facility.getInstance(), "true"));
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GODMODE_SELF_ON.getLocal());
                }
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
        } else {
            if (!cs.hasPermission("facility.command.godmode.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = Bukkit.getPlayer(args[0]);
            if (player.hasMetadata("godmode")) {
                player.removeMetadata("godmode", Facility.getInstance());
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GODMODE_OTHER_OFF.getLocal().replaceAll("%player", player.getName()));
            } else {
                player.setMetadata("godmode", new FixedMetadataValue(Facility.getInstance(), "true"));
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GODMODE_OTHER_ON.getLocal().replaceAll("%player", player.getName()));
            }
        }

        return true;
    }

}
