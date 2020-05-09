package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.spawnmob")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/spawnmob <Type> <Amount> [Spieler]"));
            return true;
        }
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(args[0].toUpperCase());
        } catch (Exception ex) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SPAWNMOB_UNKNOWNMOB.getLocal());
            return true;
        }
        if (args.length == 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            Player player = (Player) cs;
            player.getWorld().spawnEntity(player.getLocation(), entityType);
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SPAWNMOB_SPAWNED.getLocal().replaceAll("%amount", "1").replaceAll("%type", entityType.toString()));
            return true;
        }
        Integer amount;
        try {
            amount = Integer.valueOf(args[1]);
        } catch (NumberFormatException ex) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            return true;
        }
        if (args.length == 2) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            Player player = (Player) cs;
            for (int i = amount; i != 0; i--) {
                player.getWorld().spawnEntity(Facility.getInstance().getVersion().getTargetBlock(player, 50), entityType);
            }
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SPAWNMOB_SPAWNED.getLocal().replaceAll("%amount", String.valueOf(amount)).replaceAll("%type", entityType.toString()));
        }
        if (args.length == 3) {
            if (Bukkit.getPlayer(args[2]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[2]);
            for (int i = amount; i != 0; i--) {
                target.getWorld().spawnEntity(target.getLocation(), entityType);
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MISC_SPAWNMOB_OTHER.getLocal().replaceAll("%player", target.getName()).replaceAll("%amount", String.valueOf(amount)).replaceAll("%type", entityType.toString()));
        }
        return true;
    }

}
