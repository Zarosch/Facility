package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeListCommand implements CommandExecutor {

    private final Facility plugin;

    public HomeListCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.homelist")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }

        final Player player = (Player) cs;
        final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(player.getUniqueId().toString());
        if (dbPlayer.getHomes().isEmpty()) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_NOHOMES.getLocal());
            return true;
        }
        String list = "";
        for (String home : dbPlayer.getHomes().keySet()) {
            list = list + home + ",";
        }
        list = list.substring(0, list.length() - 1);
        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.HOME_LIST.getLocal().replaceAll("%list", list));
        return true;
    }

}
