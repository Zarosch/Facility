package me.velz.facility.listeners;

import me.velz.facility.commands.TpaCommand;
import me.velz.facility.objects.FacilityTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ()) {
            if (TpaCommand.getTeleportStorage().containsKey(event.getPlayer())) {
                FacilityTeleport facilityTeleport = TpaCommand.getTeleportStorage().get(event.getPlayer());
                facilityTeleport.cancel();
            }
        }
        if (event.getPlayer().hasMetadata("freeze")) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                event.getPlayer().teleport(event.getFrom());
            }
        }
    }
    
}
