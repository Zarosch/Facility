package me.velz.facility.listeners;

import me.velz.facility.commands.BlockPhysicsCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsListener implements Listener {
    
    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (!BlockPhysicsCommand.isBlockPhysics()) {
            event.setCancelled(true);
        }
    }

}
