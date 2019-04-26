package me.velz.facility.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.velz.facility.Facility;
import me.velz.facility.commands.GlobalMuteCommand;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final Facility plugin;

    public ChatListener(Facility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        //<editor-fold defaultstate="collapsed" desc="Is Globalmute on?">
        if (GlobalMuteCommand.isGlobalMute() && !event.getPlayer().hasPermission("facility.bypass.globalmute")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_GLOBALMUTE_CANCEL.getLocal());
            return;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Chat Color">
        if (event.getPlayer().hasPermission("facility.color.chat")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Is player muted?">
        DatabasePlayer dbPlayer = plugin.getDatabase().getUser(event.getPlayer().getUniqueId().toString());
        if (!dbPlayer.getMute().equalsIgnoreCase("OK")) {
            event.setCancelled(true);
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
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Build Message">
        String message = ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getChatFormat())
                .replaceAll("%player", "%s")
                .replaceAll("%prefix", plugin.getImplementations().getVault().getChat().getPlayerPrefix(event.getPlayer()));
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
        }
        String chatmessage = event.getMessage();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Chat @Mention">
        if (plugin.getFileManager().isChatMention()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (chatmessage.contains("@" + player.getName())) {
                    chatmessage = chatmessage.replaceAll("@" + player.getName(), "§b@" + player.getName() + "§f");
                    player.playSound(player.getLocation(), plugin.getVersion().getSound("BLOCK_NOTE_BELL"), 1, 1);
                } else
                if (chatmessage.contains(player.getName())) {
                    chatmessage = chatmessage.replaceAll(player.getName(), "§b@" + player.getName() + "§f");
                    player.playSound(player.getLocation(), plugin.getVersion().getSound("BLOCK_NOTE_BELL"), 1, 1);
                }
            }
        }
        //</editor-fold>
        event.setMessage(chatmessage);
        event.setFormat(message.replaceAll("%message", "%s"));
    }
}
