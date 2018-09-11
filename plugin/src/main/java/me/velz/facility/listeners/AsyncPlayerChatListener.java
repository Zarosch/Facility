package me.velz.facility.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.velz.facility.Facility;
import me.velz.facility.commands.GlobalMuteCommand;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
    
    private final Facility plugin;

    public AsyncPlayerChatListener(Facility plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (GlobalMuteCommand.isGlobalMute() && !event.getPlayer().hasPermission("facility.bypass.globalmute")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_GLOBALMUTE_CANCEL.getLocal());
            return;
        }
        if (event.getPlayer().hasPermission("facility.color.chat")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
        event.setCancelled(true);
        DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(event.getPlayer().getUniqueId().toString());
        if (!dbPlayer.getMute().equalsIgnoreCase("OK")) {
            final String[] data = dbPlayer.getMute().split(";");
            String duration;
            if (data[1].equalsIgnoreCase("-1")) {
                duration = "Permanent";
            } else {
                duration = plugin.getTools().getDateAsString(Long.valueOf(data[1]));
            }
            event.getPlayer().sendMessage(MessageUtil.PUNISH_MUTED_MESSAGE.getLocal().replaceAll("%reason", data[2]).replaceAll("%time", duration));
            return;
        }
        String message = ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getChatFormat())
                .replaceAll("%player", event.getPlayer().getName())
                .replaceAll("%prefix", plugin.getImplementations().getVault().getChat().getPlayerPrefix(event.getPlayer()));
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
        }
        String chatmessage = event.getMessage();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (chatmessage.contains(player.getName())) {
                chatmessage = chatmessage.replaceAll(player.getName(), "§b@" + player.getName() + "§f");
                player.playSound(player.getLocation(), plugin.getVersion().getSound("BLOCK_NOTE_BELL"), 1, 1);
            }
        }
        Bukkit.broadcastMessage(message.replaceAll("%message", chatmessage));
    }
}
