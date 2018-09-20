package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FacilityCommand implements CommandExecutor {

    private final Facility plugin;

    public FacilityCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.admin")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FACILITY_HELP_HEADER.getLocal());
            cs.sendMessage(MessageUtil.FACILITY_HELP_RELOAD.getLocal());
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.getFileManager().load();
            MessageUtil.load();
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FACILITY_RELOAD.getLocal());
            return true;
        }
        return true;
    }

}
