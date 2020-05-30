package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    
    private final Facility plugin;

    public SetHomeCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.sethome")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }

        final Player player = (Player) cs;
        final String home;
        if (args.length == 1) {
            home = args[0];
        } else {
            home = "home";
        }
        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(player.getUniqueId().toString());
            if(dbPlayer.getHomes().containsKey(home)) {
                dbPlayer.deleteHome(home);
            }
            dbPlayer.addHome(home, player.getLocation());
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_SET.getLocal().replaceAll("%home", home));
        });
        return true;
    }

}
