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

public class BanCommand implements CommandExecutor {
    
    private final Facility plugin;

    public BanCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.ban")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length < 2) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/ban <Spieler> <Grund>"));
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[0]);
            if (!dbPlayer.isSuccess()) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            } else if (!dbPlayer.getBan().equalsIgnoreCase("OK")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_ALREADYBANNED.getLocal());
            } else {
                String reason = "";
                for (int i = 1; i != args.length; i++) {
                    reason = reason + args[i] + " ";
                }
                reason = reason.substring(0, reason.length() - 1);
                final String r = reason;
                TextComponent component = new TextComponent(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_BANNEDBROADCAST.getLocal().replaceAll("%reason", reason).replaceAll("%name", args[0]));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.PUNISH_BANNEDHOVER.getLocal().replaceAll("%punisher", cs.getName()).replaceAll("%time", "Permanent")).create()));
                dbPlayer.setBan("BLOCKED;-1;" + reason);
                dbPlayer.save();
                Bukkit.getOnlinePlayers().stream().filter((all) -> (all.hasPermission("facility.broadcast.ban") || all.hasPermission("facility.broadcast.punish") || all.hasPermission("facility.command.ban"))).forEachOrdered((all) -> {
                    Facility.getInstance().getVersion().sendComponentMessage(all, component);
                });
                if (Bukkit.getPlayer(args[0]) != null) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Player target = Bukkit.getPlayer(args[0]);
                        target.kickPlayer(MessageUtil.PUNISH_BANNEDSCREEN.getLocal().replaceAll("%reason", r).replaceAll("%time", "Permanent"));
                    });
                }
            }
        });
        return true;
    }

}
