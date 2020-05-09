package me.velz.facility.commands;

import java.util.ArrayList;
import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class MsgCommand implements CommandExecutor {
    
    private final Facility plugin;

    public MsgCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Getter
    private static final ArrayList<Player> SOCIALSPY = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.msg")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0 || args.length == 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/msg <Spieler> <Nachricht>"));
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
            return true;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (cs instanceof Player) {
            if ((Player) cs == target) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_MSG_TARGETSELF.getLocal());
                return true;
            }
        }
        int i = 0;
        String message = "";
        for (String msg : args) {
            if (i != 0) {
                message = message + " " + msg;
            }
            i++;
        }
        message = message.substring(1, message.length());
        cs.sendMessage(MessageUtil.CHAT_MSG_SELF.getLocal().replaceAll("%player", target.getName()).replaceAll("%message", message));
        target.sendMessage(MessageUtil.CHAT_MSG_TARGET.getLocal().replaceAll("%player", cs.getName()).replaceAll("%message", message));
        if (!MsgCommand.getSOCIALSPY().isEmpty()) {
            for (Player spyer : MsgCommand.getSOCIALSPY()) {
                spyer.sendMessage(MessageUtil.CHAT_SOCIALSPY_MSG.getLocal().replaceAll("%player", cs.getName()).replaceAll("%target", target.getName()).replaceAll("%message", message));
            }
        }
        target.playSound(target.getLocation(), Facility.getInstance().getVersion().getSound("BLOCK_NOTE_BELL"), 1, 1);
        if (cs instanceof Player) {
            final Player player = (Player) cs;
            player.setMetadata("reply", new FixedMetadataValue(plugin, target.getUniqueId().toString()));
            target.setMetadata("reply", new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        }
        return true;
    }

}
