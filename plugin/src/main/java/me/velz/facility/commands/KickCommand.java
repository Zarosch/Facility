package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.kick")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length < 2) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/kick <Spieler> <Grund>"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (cs instanceof Player) {
            if (target.hasPermission("facility.bypass.punishments")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.PUNISH_BYPASSPUNISH.getLocal());
                return true;
            }
        }
        String reason = "";
        for (int i = 1; i != args.length; i++) {
            reason = reason + args[i] + " ";
        }
        reason = reason.substring(0, reason.length() - 1);
        target.kickPlayer(MessageUtil.PUNISH_KICKSCREEN.getLocal().replaceAll("%reason", reason));
        TextComponent component = new TextComponent(MessageUtil.PUNISH_KICKED.getLocal().replaceAll("%reason", reason).replaceAll("%name", target.getName()));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageUtil.PUNISH_KICKEDHOVER.getLocal().replaceAll("%punisher", cs.getName())).create()));
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all.hasPermission("facility.broadcast.kick") || all.hasPermission("facility.broadcast.punish") || all.hasPermission("facility.command.kick")) {
                Facility.getInstance().getVersion().sendComponentMessage(all, component);
            }
        }
        return true;
    }

}
