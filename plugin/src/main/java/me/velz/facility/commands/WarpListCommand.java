package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WarpListCommand implements CommandExecutor {

    private final Facility plugin;

    public WarpListCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.warplist")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (!plugin.getWarps().isEmpty()) {
            String warps = "";
            for (String warp : plugin.getWarps().keySet()) {
                warps = warps + warp + ", ";
            }
            warps = warps.substring(0, warps.length() - 2);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_LIST.getLocal().replaceAll("%list", warps));
        } else {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOWARPS.getLocal());
        }

        return true;
    }

}
