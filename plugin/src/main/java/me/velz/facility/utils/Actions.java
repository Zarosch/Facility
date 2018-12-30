package me.velz.facility.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Actions {
    
    public static void run(Player player, String string) {
        String[] act = string.split(">");
        String action = act[0];
        String message = act[1];
        if(action.equalsIgnoreCase("message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        if(action.equalsIgnoreCase("command")) {
            Bukkit.dispatchCommand(player, message);
        }
    }
    
}
