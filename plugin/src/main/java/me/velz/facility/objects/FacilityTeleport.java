package me.velz.facility.objects;

import me.velz.facility.commands.TpaCommand;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FacilityTeleport {

    public Player player;
    public Player target = null;
    private Location to;
    private final String message;
    public Integer seconds;

    public FacilityTeleport(Player player, Location to, String message, Integer seconds) {
        this.player = player;
        this.to = to;
        this.message = message;
        this.seconds = seconds;
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TELEPORTSTART.getLocal().replaceAll("%seconds", String.valueOf(seconds)));
        TpaCommand.getTeleportStorage().put(player, this);
    }

    public FacilityTeleport(Player player, Player target, String message, Integer seconds) {
        this.player = player;
        this.target = target;
        this.message = message;
        this.seconds = seconds;
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TELEPORTSTART.getLocal().replaceAll("%seconds", String.valueOf(seconds)));
        TpaCommand.getTeleportStorage().put(player, this);
    }

    //<editor-fold defaultstate="collapsed" desc="teleport">
    public void teleport() {
        if (target != null) {
            player.teleport(target);
        } else {
            player.teleport(to);
        }
        player.sendMessage(message);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cancel">
    public void cancel() {
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TELEPORT_TELEPORTCANCEL.getLocal());
        TpaCommand.getTeleportStorage().remove(player);
    }
    //</editor-fold>

}
