package me.velz.facility.listeners;

import me.velz.facility.commands.TpaCommand;
import me.velz.facility.objects.FacilityTeleport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //<editor-fold defaultstate="collapsed" desc="Facility Teleport dont Move!">
        if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ()) {
            if (TpaCommand.getTeleportStorage().containsKey(event.getPlayer())) {
                FacilityTeleport facilityTeleport = TpaCommand.getTeleportStorage().get(event.getPlayer());
                facilityTeleport.cancel();
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Freeze">
        if (event.getPlayer().hasMetadata("freeze")) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                event.getPlayer().teleport(event.getFrom());
            }
        }
        //</editor-fold>
    }
    
}
