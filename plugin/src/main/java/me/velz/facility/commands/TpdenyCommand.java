package me.velz.facility.commands;

import me.velz.facility.objects.FacilityTpa;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpdenyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.tpdeny")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tpdeny <Spieler>"));
            return true;
        }
        if (args.length == 1) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player player = (Player) cs;
            Player target = Bukkit.getPlayer(args[0]);
            if (!TpaCommand.getTpaStorage().containsKey(target)) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_NOREQUEST.getLocal());
                return true;
            }
            FacilityTpa facilityTpa = TpaCommand.getTpaStorage().get(target);
            if (facilityTpa.getPlayer2() != player) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_NOREQUEST.getLocal());
                return true;
            }
            target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_DENIED_PLAYER.getLocal().replaceAll("%player", player.getName()));
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TPA_DENIED_TARGET.getLocal().replaceAll("%player", target.getName()));
            TpaCommand.getTpaStorage().remove(target);
        }
        return true;
    }

}
