package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.speed")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/speed <Speedlevel> [Spieler]"));
            return true;
        }
        if (args.length == 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            Player player = (Player) cs;
            if (player.isFlying()) {
                try {
                    Float speed = Float.valueOf("0." + args[0]);
                    player.setFlySpeed(speed);
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_SPEED_SELF_FLY.getLocal().replaceAll("%speed", String.valueOf(speed)));
                } catch (NumberFormatException ex) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                    return true;
                }
            } else {
                try {
                    Float speed = Float.valueOf("0." + args[0]);
                    player.setWalkSpeed(speed);
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_SPEED_SELF_WALK.getLocal().replaceAll("%speed", String.valueOf(speed)));
                } catch (NumberFormatException ex) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                    return true;
                }
            }
        } else {
            if (!cs.hasPermission("facility.command.speed.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player.isFlying()) {
                try {
                    Float speed = Float.valueOf("0." + args[0]);
                    player.setFlySpeed(speed);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_SPEED_OTHER_FLY.getLocal().replaceAll("%speed", String.valueOf(speed)).replaceAll("%player", player.getName()));
                } catch (NumberFormatException ex) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                    return true;
                }
            } else {
                try {
                    Float speed = Float.valueOf("0." + args[0]);
                    player.setWalkSpeed(speed);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_SPEED_OTHER_WALK.getLocal().replaceAll("%speed", String.valueOf(speed)).replaceAll("%player", player.getName()));
                } catch (NumberFormatException ex) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                    return true;
                }
            }
        }
        return true;
    }

}
