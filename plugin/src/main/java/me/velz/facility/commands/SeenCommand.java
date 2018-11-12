package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeenCommand implements CommandExecutor {

    private final Facility plugin;

    public SeenCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.seen")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/seen <Player>"));
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[0]);
            if (dbPlayer.isSuccess()) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_SEEN_HEADER.getLocal().replaceAll("%player", dbPlayer.getName()));
                final String lastSeen;
                if (dbPlayer.isOnline()) {
                    lastSeen = "Jetzt";
                } else {
                    lastSeen = Facility.getInstance().getTools().getDateAsString(dbPlayer.getLastJoin());
                }
                cs.sendMessage(MessageUtil.PLAYER_SEEN_MESSAGE.getLocal()
                        .replaceAll("%playtime", Facility.getInstance().getTools().getPlaytime(dbPlayer.getPlaytime()))
                        .replaceAll("%money", dbPlayer.getMoney() + " " + MessageUtil.MONEYNAME.getLocal())
                        .replaceAll("%firstjoin", Facility.getInstance().getTools().getDateAsString(dbPlayer.getFirstJoin()))
                        .replaceAll("%lastjoin", lastSeen));
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            }
        });
        return true;
    }

}
