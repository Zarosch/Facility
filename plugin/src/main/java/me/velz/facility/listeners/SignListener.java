package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityTeleport;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    private final Facility plugin;

    public SignListener(Facility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission(plugin.getFileManager().getPermissionPrefix() + ".color.signs")) {
            int i = 0;
            for (String line : event.getLines()) {
                event.setLine(i, net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', line));
                i++;
            }
        }
        if (!event.getPlayer().hasPermission(plugin.getFileManager().getPermissionPrefix() + ".sign.warp")) {
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

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (plugin.getVersion().isSign(event.getClickedBlock().getType())) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1).equalsIgnoreCase("§8[§1Warp§8]")) {
                    String warp = sign.getLine(2);
                    if (!plugin.getWarps().containsKey(warp)) {
                        event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOTFOUND.getLocal());
                        return;
                    }
                    if (event.getPlayer().hasPermission(plugin.getFileManager().getPermissionPrefix() + ".bypass.teleportdelay")) {
                        event.getPlayer().teleport(plugin.getWarps().get(warp).getLoc());
                        event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_TELEPORT_SELF.getLocal().replaceAll("%warp", warp));
                    } else {
                        new FacilityTeleport(event.getPlayer(), plugin.getWarps().get(warp).getLoc(), MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_TELEPORT_SELF.getLocal().replaceAll("%warp", warp), plugin.getFileManager().getTeleportDelay());
                    }
                }
            }
        }
    }

}
