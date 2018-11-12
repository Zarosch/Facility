package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.commands.MsgCommand;
import me.velz.facility.commands.TpaCommand;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final Facility plugin;

    public PlayerQuitListener(Facility plugin) {
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (MessageUtil.SERVER_QUIT.getLocal().equalsIgnoreCase("null")) {
            event.setQuitMessage(null);
        } else {
            event.setQuitMessage(MessageUtil.SERVER_QUIT.getLocal().replaceAll("%player", event.getPlayer().getName()));
        }
        if (event.getPlayer().hasMetadata("reply")) {
            event.getPlayer().removeMetadata("reply", plugin);
        }
        if (event.getPlayer().hasMetadata("vanish")) {
            event.getPlayer().removeMetadata("vanish", plugin);
        }
        if (event.getPlayer().hasMetadata("godmode")) {
            event.getPlayer().removeMetadata("godmode", plugin);
        }
        if (event.getPlayer().hasMetadata("freeze")) {
            event.getPlayer().removeMetadata("freeze", Facility.getInstance());
        }
        if (MsgCommand.getSOCIALSPY().contains(event.getPlayer())) {
            MsgCommand.getSOCIALSPY().remove(event.getPlayer());
        }
        if (TpaCommand.getTeleportStorage().containsKey(event.getPlayer())) {
            TpaCommand.getTeleportStorage().remove(event.getPlayer());
        }
        TpaCommand.getTpaStorage().values().stream().filter((facilityTpa) -> (event.getPlayer() == facilityTpa.getPlayer2())).forEachOrdered((facilityTpa) -> {
            TpaCommand.getTpaStorage().remove(facilityTpa.getPlayer1());
        });
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.getPlayers().containsKey(event.getPlayer().getUniqueId().toString())) {
                plugin.getDatabase().saveUser(event.getPlayer().getUniqueId().toString(), plugin.getPlayers().get(event.getPlayer().getUniqueId().toString()));
                plugin.getPlayers().remove(event.getPlayer().getUniqueId().toString());
            }
        });
    }

}
