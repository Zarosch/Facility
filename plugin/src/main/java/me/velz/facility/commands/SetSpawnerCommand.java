package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SetSpawnerCommand implements CommandExecutor {

    private final Facility plugin;

    public SetSpawnerCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.setspawner")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/setspawner <type>"));
            return true;
        }
        Player player = (Player) cs;
        Block target = player.getTargetBlock(null, 20);
        if (target == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_TARGET.getLocal());
            return true;
        }
        if (target.getType() != plugin.getVersion().getMaterial("MOB_SPAWNER")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_TARGET.getLocal());
            return true;
        }
        if (EntityType.valueOf(args[0].toUpperCase()) != null) {
            CreatureSpawner creature = (CreatureSpawner) target.getState();
            creature.setSpawnedType(EntityType.valueOf(args[0].toUpperCase()));
            creature.update();
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_CHANGED.getLocal().replaceAll("%type", creature.getType().toString()));
        } else {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_TYPE.getLocal());
            return true;
        }
        return true;
    }

}
