package me.velz.facility.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SetSpawnerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.setspawner")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/setspawner <Type>"));
            return true;
        }
        Player player = (Player) cs;
        Set<Material> set = new HashSet<>(Arrays.asList(Material.MOB_SPAWNER));
        Block target = player.getTargetBlock(null, 20);
        if (target == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_TARGET.getLocal());
            return true;
        }
        if (target.getType() != Material.MOB_SPAWNER) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_TARGET.getLocal());
            return true;
        }
        if (EntityType.valueOf(args[0]) != null) {
            CreatureSpawner creature = (CreatureSpawner) target.getState();
            creature.setSpawnedType(EntityType.valueOf(args[0]));
            creature.update();
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_CHANGED.getLocal());
        } else {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SETSPAWNER_TYPE.getLocal());
            return true;
        }
        return true;
    }

}
