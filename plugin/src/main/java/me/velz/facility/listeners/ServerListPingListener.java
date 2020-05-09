package me.velz.facility.listeners;

import java.util.Random;
import me.velz.facility.Facility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {

    private final Facility plugin;

    public ServerListPingListener(Facility plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        final Random random = new Random();
        final int rand = random.nextInt(plugin.getFileManager().getMotds().size());
        final String motd = plugin.getFileManager().getMotds().get(rand);
        event.setMotd(motd);
    }

}
