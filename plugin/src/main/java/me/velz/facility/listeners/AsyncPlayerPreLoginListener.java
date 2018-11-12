package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener implements Listener {

    private final Facility plugin;

    public AsyncPlayerPreLoginListener(Facility plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onAsyncPrePlayerLogin(AsyncPlayerPreLoginEvent event) {
        if (!plugin.getDatabase().issetUser(event.getUniqueId().toString())) {
            plugin.getDatabase().insertUser(event.getUniqueId().toString(), event.getName());
        }
        DatabasePlayer dbPlayer = plugin.getDatabase().loadUser(event.getUniqueId().toString(), event.getName());
        plugin.getPlayers().put(event.getUniqueId().toString(), dbPlayer);
    }

}
