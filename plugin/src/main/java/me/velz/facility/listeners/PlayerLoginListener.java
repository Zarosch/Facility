package me.velz.facility.listeners;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {
    
    private final Facility plugin;

    public PlayerLoginListener(Facility plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(event.getPlayer().getUniqueId().toString());
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
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (event.getPlayer().hasPermission("facility.bypass.fullserver")) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
            } else {
                event.setKickMessage(MessageUtil.SERVER_FULL.getLocal());
            }
        }
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            dbPlayer.setLastJoin(System.currentTimeMillis());
        }
    }

}
