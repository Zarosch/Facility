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

public class UnBanCommand implements CommandExecutor {
    
    private final Facility plugin;

    public UnBanCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.unban")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length != 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/unban <Spieler>"));
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[0]);
            if (!dbPlayer.isSuccess()) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            } else if (dbPlayer.getBan().equalsIgnoreCase("OK")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_NOTBANNED.getLocal());
            } else {
                TextComponent component = new TextComponent(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_UNBANNED.getLocal().replaceAll("%name", args[0]));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.PUNISH_UNBANNEDHOVER.getLocal().replaceAll("%punisher", cs.getName())).create()));
                dbPlayer.setBan("OK");
                dbPlayer.save();
                Bukkit.getOnlinePlayers().stream().filter((all) -> (all.hasPermission("facility.broadcast.unban") || all.hasPermission("facility.broadcast.punish") || all.hasPermission("facility.commands.unban"))).forEachOrdered((all) -> {
                    Facility.getInstance().getVersion().sendComponentMessage(all, component);
                });
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_UNBAN.getLocal());
            }
        });
        return true;
    }

}
