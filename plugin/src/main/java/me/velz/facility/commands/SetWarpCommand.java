package me.velz.facility.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import me.velz.facility.database.DatabaseWarp;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    private final Facility plugin;

    public SetWarpCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.setwarp")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length != 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/setwarp <Warp>"));
            return true;
        }
        if (plugin.getWarps().containsKey(args[0])) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_FOUND.getLocal());
            return true;
        }
        if (cs instanceof Player) {
            Player player = (Player) cs;
            DatabaseWarp warp = new DatabaseWarp(args[0], player.getLocation());
            Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                Connection connection = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    connection = plugin.getDatabase().getConnection();
                    String query = "SELECT * from warps WHERE name = ?";
                    ps = connection.prepareStatement(query);
                    ps.setString(1, args[0]);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        query = "UPDATE warps SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE name = ?";
                        ps = connection.prepareStatement(query);
                        ps.setString(1, player.getLocation().getWorld().getName());
                        ps.setDouble(2, player.getLocation().getX());
                        ps.setDouble(3, player.getLocation().getY());
                        ps.setDouble(4, player.getLocation().getZ());
                        ps.setFloat(5, player.getLocation().getYaw());
                        ps.setFloat(6, player.getLocation().getPitch());
                        ps.setString(7, args[0]);
                        ps.executeUpdate();
                        ps.close();
                    } else {
                        query = "INSERT INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        ps = connection.prepareStatement(query);
                        ps.setString(1, args[0]);
                        ps.setString(2, player.getLocation().getWorld().getName());
                        ps.setDouble(3, player.getLocation().getX());
                        ps.setDouble(4, player.getLocation().getY());
                        ps.setDouble(5, player.getLocation().getZ());
                        ps.setFloat(6, player.getLocation().getYaw());
                        ps.setFloat(7, player.getLocation().getPitch());
                        ps.executeUpdate();
                        ps.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(SetWarpCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
                plugin.getWarps().put(args[0], warp);
            });
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_SET.getLocal().replaceAll("%warp", args[0]));
        } else {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }
        return true;
    }

}
