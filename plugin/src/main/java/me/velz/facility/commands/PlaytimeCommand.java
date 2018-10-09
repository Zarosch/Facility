package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaytimeCommand implements CommandExecutor {

    private final Facility plugin;

    public PlaytimeCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.playtime")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (args.length == 0) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }

            final Player player = (Player) cs;
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(player.getUniqueId().toString());
                    final String playtime = plugin.getTools().getPlaytime(dbPlayer.getPlaytime());
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYTIME_SELF.getLocal().replaceAll("%playtime", playtime));
                }
            });
            return true;
        } else {
            if (!cs.hasPermission("facility.command.playtime.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(args[0]);
                    if (dbPlayer.isSuccess()) {
                        final String playtime = Facility.getInstance().getTools().getPlaytime(dbPlayer.getPlaytime());
                        if (!dbPlayer.isOnline()) {
                            dbPlayer.save();
                        }
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYTIME_OTHER.getLocal().replaceAll("%playtime", playtime).replaceAll("%player", args[0]));
                    } else {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    }
                }
            });
        }
        return true;
    }

}
