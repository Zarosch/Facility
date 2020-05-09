package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class VanishCommand implements CommandExecutor {

    private final Facility plugin;

    public VanishCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.vanish")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player player = (Player) cs;
                if (player.hasMetadata("vanish")) {
                    player.removeMetadata("vanish", Facility.getInstance());
                    Bukkit.getOnlinePlayers().forEach((all) -> {
                        plugin.getVersion().showPlayer(all, player);
                    });
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_VANISH_SELF_OFF.getLocal());
                } else {
                    player.setMetadata("vanish", new FixedMetadataValue(Facility.getInstance(), "true"));
                    Bukkit.getOnlinePlayers().forEach((all) -> {
                        plugin.getVersion().hidePlayer(all, player);
                    });
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_VANISH_SELF_ON.getLocal());
                }
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
        } else {
            if (!cs.hasPermission("facility.command.vanish.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = Bukkit.getPlayer(args[0]);
            if (player.hasMetadata("vanish")) {
                player.removeMetadata("vanish", Facility.getInstance());
                Bukkit.getOnlinePlayers().forEach((all) -> {
                    plugin.getVersion().showPlayer(all, player);
                });
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_VANISH_OTHER_OFF.getLocal().replaceAll("%player", player.getName()));
            } else {
                player.setMetadata("vanish", new FixedMetadataValue(Facility.getInstance(), "true"));
                Bukkit.getOnlinePlayers().forEach((all) -> {
                    plugin.getVersion().hidePlayer(all, player);
                });
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_VANISH_OTHER_ON.getLocal().replaceAll("%player", player.getName()));
            }
        }

        return true;
    }

}
