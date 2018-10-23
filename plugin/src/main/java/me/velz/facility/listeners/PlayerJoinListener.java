package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    private final Facility plugin;

    public PlayerJoinListener(Facility plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (MessageUtil.SERVER_JOIN.getLocal().equalsIgnoreCase("null")) {
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage(MessageUtil.SERVER_JOIN.getLocal().replaceAll("%player", event.getPlayer().getName()));
        }
        event.getPlayer().sendMessage(MessageUtil.CHAT_MOTD.getLocal().replaceAll("%player", event.getPlayer().getName()));
        if (!event.getPlayer().hasPlayedBefore()) {
            if (plugin.getFileManager().getSpawnLocation() != null) {
                event.getPlayer().teleport(plugin.getFileManager().getSpawnLocation());
            }
        }
        Bukkit.getOnlinePlayers().stream().filter((all) -> (all.hasMetadata("vanish"))).forEachOrdered((all) -> {
            plugin.getVersion().hidePlayer(all, event.getPlayer());
            plugin.getVersion().showPlayer(all, event.getPlayer());
            plugin.getVersion().hidePlayer(event.getPlayer(), all);
        });
        //plugin.getVersion().setTablist(event.getPlayer(), MessageUtil.SERVER_TABLIST_HEADER.getLocal(), MessageUtil.SERVER_TABLIST_FOOTER.getLocal());
    }

}
