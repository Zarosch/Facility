package me.velz.facility.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("facility.color.signs")) {
            int i = 0;
            for (String line : event.getLines()) {
                event.setLine(i, net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', line));
                i++;
            }
        }
    }

}
