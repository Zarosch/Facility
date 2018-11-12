package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityArmorstand;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final Facility plugin;

    public PlayerInteractListener(Facility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == plugin.getVersion().getMaterial("SIGN_POST")) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase("§8[§1Warp§8]")) {
                    String warp = sign.getLine(2);
                    if (!plugin.getWarps().containsKey(warp)) {
                        event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOTFOUND.getLocal());
                        return;
                    }
                    if (event.getPlayer().hasPermission("facility.bypass.teleportdelay")) {
                        event.getPlayer().teleport(plugin.getWarps().get(warp).getLoc());
                        event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_TELEPORT_SELF.getLocal().replaceAll("%warp", warp));
                    } else {
                        new FacilityTeleport(event.getPlayer(), plugin.getWarps().get(warp).getLoc(), MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_TELEPORT_SELF.getLocal().replaceAll("%warp", warp), plugin.getFileManager().getTeleportDelay());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArmorstand(PlayerArmorStandManipulateEvent event) {
        if (event.getRightClicked().getCustomName() == null) {
            return;
        }
        for (FacilityArmorstand armorStand : plugin.getArmorstands().values()) {
            if (armorStand.getName().equalsIgnoreCase(event.getRightClicked().getName())) {
                if (event.getPlayer().hasPermission(armorStand.getPermission())) {
                    armorStand.use(event.getPlayer());
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorstand(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            if (event.getRightClicked().getCustomName() == null) {
                return;
            }
            for (FacilityArmorstand armorStand : plugin.getArmorstands().values()) {
                if (armorStand.getName().equalsIgnoreCase(event.getRightClicked().getName())) {
                    if (event.getPlayer().hasPermission(armorStand.getPermission())) {
                        armorStand.use(event.getPlayer());
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

}
