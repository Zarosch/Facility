package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {

    private final Facility plugin;

    public SignChangeListener(Facility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("facility.color.signs")) {
            int i = 0;
            for (String line : event.getLines()) {
                event.setLine(i, net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', line));
                i++;
            }
        }
        if (!event.getPlayer().hasPermission("facility.sign.warp")) {
            if (event.getLine(1).equalsIgnoreCase("[Warp]") || event.getLine(1).equalsIgnoreCase("&8[&1Warp&8]")) {
                event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                event.setCancelled(true);
            }
        } else {
            if (event.getLine(1).equalsIgnoreCase("[Warp]") || event.getLine(1).equalsIgnoreCase("&8[&1Warp&8]")) {
                if (!plugin.getWarps().containsKey(event.getLine(2))) {
                    event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOTFOUND.getLocal());
                    event.setCancelled(true);
                } else {
                    event.setLine(1, "§8[§1Warp§8]");
                }
            }
        }
    }

}
