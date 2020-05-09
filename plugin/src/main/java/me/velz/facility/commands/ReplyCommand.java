package me.velz.facility.commands;

import java.util.UUID;
import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ReplyCommand implements CommandExecutor {

    private final Facility plugin;

    public ReplyCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.msg")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/r <Nachricht>"));
            return true;
        }
        final Player player = (Player) cs;
        if (!player.hasMetadata("reply")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_MSG_NOTFOUND.getLocal());
            return true;
        }
        if (Bukkit.getPlayer(UUID.fromString(player.getMetadata("reply").get(0).asString())) == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            return true;
        }
        final Player target = Bukkit.getPlayer(UUID.fromString(player.getMetadata("reply").get(0).asString()));
        String message = "";
        for (String msg : args) {
            message = message + " " + msg;
        }
        message = message.substring(1, message.length());
        if (!MsgCommand.getSOCIALSPY().isEmpty()) {
            for (Player spyer : MsgCommand.getSOCIALSPY()) {
                spyer.sendMessage(MessageUtil.CHAT_SOCIALSPY_MSG.getLocal().replaceAll("%player", cs.getName()).replaceAll("%target", target.getName()).replaceAll("%message", message));
            }
        }
        cs.sendMessage(MessageUtil.CHAT_MSG_SELF.getLocal().replaceAll("%player", target.getName()).replaceAll("%message", message));
        target.sendMessage(MessageUtil.CHAT_MSG_TARGET.getLocal().replaceAll("%player", cs.getName()).replaceAll("%message", message));
        target.playSound(target.getLocation(), Facility.getInstance().getVersion().getSound("BLOCK_NOTE_BELL"), 1, 1);
        if (cs instanceof Player) {
            player.setMetadata("reply", new FixedMetadataValue(plugin, target.getUniqueId().toString()));
            target.setMetadata("reply", new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        }
        return true;
    }

}
