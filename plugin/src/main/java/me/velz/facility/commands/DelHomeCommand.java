package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {

    private final Facility plugin;

    public DelHomeCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.delhome")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length != 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/delhome <Name>"));
            return true;
        }

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }

        final Player player = (Player) cs;

        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(player.getUniqueId().toString());
            if (!dbPlayer.getHomes().containsKey(args[0])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_NOTFOUND.getLocal());
            } else {
                dbPlayer.deleteHome(args[0]);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_DELETE.getLocal().replaceAll("%home", args[0]));
            }
        });
        return true;
    }

}
