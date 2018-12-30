package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListener implements Listener {

    private final Facility plugin;

    public JoinListener(Facility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPrePlayerLogin(AsyncPlayerPreLoginEvent event) {
        //<editor-fold defaultstate="collapsed" desc="Insert player to database">
        if (!plugin.getDatabase().issetUser(event.getUniqueId().toString())) {
            plugin.getDatabase().insertUser(event.getUniqueId().toString(), event.getName());
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Load player from database">
        DatabasePlayer dbPlayer = plugin.getDatabase().loadUser(event.getUniqueId().toString(), event.getName());
        plugin.getPlayers().put(event.getUniqueId().toString(), dbPlayer);
        //</editor-fold>
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(event.getPlayer().getUniqueId().toString());
        //<editor-fold defaultstate="collapsed" desc="Is Banned?">
        if (!dbPlayer.getBan().equalsIgnoreCase("OK")) {
            String[] banSplitter = dbPlayer.getBan().split(";");
            Long time = Long.valueOf(banSplitter[1]);
            String reason = banSplitter[2];
            String stringTime = "Permanent";
            if (time != -1) {
                stringTime = plugin.getTools().getDate(time);
            }
            if (time != -1 && time <= System.currentTimeMillis()) {
                dbPlayer.setBan("OK");
                dbPlayer.save();
            } else {
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(MessageUtil.PUNISH_BANNEDSCREEN.getLocal().replaceAll("%reason", reason).replaceAll("%time", stringTime));
                return;
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Server Full?">
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (event.getPlayer().hasPermission("facility.bypass.fullserver")) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
            } else {
                event.setKickMessage(MessageUtil.SERVER_FULL.getLocal());
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Set last Join">
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            dbPlayer.setLastJoin(System.currentTimeMillis());
        }
        //</editor-fold>
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //<editor-fold defaultstate="collapsed" desc="Join Message">
        if (MessageUtil.SERVER_JOIN.getLocal().equalsIgnoreCase("null")) {
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage(MessageUtil.SERVER_JOIN.getLocal().replaceAll("%player", event.getPlayer().getName()));
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Chat Motd">
        event.getPlayer().sendMessage(MessageUtil.CHAT_MOTD.getLocal().replaceAll("%player", event.getPlayer().getName()));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Teleport players they not before joined to Spawn">
        if (!event.getPlayer().hasPlayedBefore()) {
            if (plugin.getFileManager().getSpawnLocation() != null) {
                event.getPlayer().teleport(plugin.getFileManager().getSpawnLocation());
            }
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Vanish">
        Bukkit.getOnlinePlayers().stream().filter((all) -> (all.hasMetadata("vanish"))).forEachOrdered((all) -> {
            plugin.getVersion().hidePlayer(all, event.getPlayer());
            plugin.getVersion().showPlayer(all, event.getPlayer());
            plugin.getVersion().hidePlayer(event.getPlayer(), all);
        });
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Set Tablist">
        plugin.getVersion().setTablist(event.getPlayer(), MessageUtil.SERVER_TABLIST_HEADER.getLocal(), MessageUtil.SERVER_TABLIST_FOOTER.getLocal());
        //</editor-fold>
    }

}
