package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {
    
    private final Facility plugin;

    public MuteCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.mute")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length < 2) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/mute <player> <reason>"));
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[0]);
            if (!dbPlayer.isSuccess()) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            } else if (!dbPlayer.getMute().equalsIgnoreCase("OK")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_ALREADYMUTED.getLocal());
            } else {
                String reason = "";
                for (int i = 1; i != args.length; i++) {
                    reason = reason + args[i] + " ";
                }
                reason = reason.substring(0, reason.length() - 1);
                final String r = reason;
                TextComponent component = new TextComponent(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_MUTEDBROADCAST.getLocal().replaceAll("%reason", reason).replaceAll("%name", args[0]));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.PUNISH_MUTEDHOVER.getLocal().replaceAll("%punisher", cs.getName()).replaceAll("%time", "Permanent")).create()));
                dbPlayer.setMute("BLOCKED;-1;" + reason);
                dbPlayer.save();
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".broadcast.mute") || all.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".broadcast.punish") || all.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.mute")) {
                        Facility.getInstance().getVersion().sendComponentMessage(all, component);
                    }
                }
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player target = Bukkit.getPlayer(args[0]);
                    target.sendMessage(MessageUtil.PUNISH_MUTED_MESSAGE.getLocal().replaceAll("%reason", r).replaceAll("%time", "Permanent"));
                }
            }
        });
        return true;
    }

}
