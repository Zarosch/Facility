package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.gamemode")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/gamemode <0/1/2/3> [Spieler]"));
            return true;
        }
        if (args.length == 1) {
            if (cs instanceof Player) {
                Player player = (Player) cs;
                if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_SELF_SURVIVAL.getLocal());
                    return true;
                }
                if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")) {
                    player.setGameMode(GameMode.CREATIVE);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_SELF_CREATIVE.getLocal());
                    return true;
                }
                if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")) {
                    player.setGameMode(GameMode.ADVENTURE);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_SELF_ADVENTURE.getLocal());
                    return true;
                }
                if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_SELF_SPECTATOR.getLocal());
                    return true;
                }
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/gamemode <0/1/2/3> [Spieler]"));
            } else {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
        } else {
            if (!cs.hasPermission("facility.command.gamemode.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {
                player.setGameMode(GameMode.SURVIVAL);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_OTHER_SURVIVAL.getLocal().replaceAll("%player", player.getName()));
                return true;
            }
            if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")) {
                player.setGameMode(GameMode.CREATIVE);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_OTHER_CREATIVE.getLocal().replaceAll("%player", player.getName()));
                return true;
            }
            if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")) {
                player.setGameMode(GameMode.ADVENTURE);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_OTHER_ADVENTURE.getLocal().replaceAll("%player", player.getName()));
                return true;
            }
            if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("spec")) {
                player.setGameMode(GameMode.SPECTATOR);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PLAYER_GAMEMODE_OTHER_SPECTATOR.getLocal().replaceAll("%player", player.getName()));
                return true;
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/gamemode <0/1/2/3> [Spieler]"));
        }

        return true;
    }

}
