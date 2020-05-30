package me.velz.facility.commands;

import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlockPhysicsCommand implements CommandExecutor {

    private final Facility plugin;

    public BlockPhysicsCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Getter
    private static boolean blockPhysics = true;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.blockphysics")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (BlockPhysicsCommand.blockPhysics) {
            BlockPhysicsCommand.blockPhysics = false;
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_BLOCKPHYSICS_OFF.getLocal());
        } else {
            BlockPhysicsCommand.blockPhysics = true;
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_BLOCKPHYSICS_ON.getLocal());
        }
        return true;
    }

}
