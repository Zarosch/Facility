package me.velz.facility.listeners;

import me.velz.facility.Facility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    private final Facility plugin;

    public RespawnListener(Facility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (plugin.getFileManager().getSpawnLocation() != null) {
            event.setRespawnLocation(plugin.getFileManager().getSpawnLocation());
        }
    }

}
