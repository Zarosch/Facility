package me.velz.facility.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarpCommand implements CommandExecutor {
    
    private final Facility plugin;

    public DelWarpCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.delwarp")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length != 1) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/delwarp <Warp>"));
            return true;
        }
        if (!plugin.getWarps().containsKey(args[0])) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_NOTFOUND.getLocal());
            return true;
        }
        if (cs instanceof Player) {
            plugin.getWarps().remove(args[0]);
            Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Connection connection = plugin.getDatabase().getConnection();
                    try {
                        String query = "SELECT * FROM warps WHERE name = ?";
                        PreparedStatement ps = plugin.getDatabase().getConnection().prepareStatement(query);
                        ps.setString(1, args[0]);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            query = "DELETE FROM warps WHERE name = ?";
                            ps = connection.prepareStatement(query);
                            ps.setString(1, args[0]);
                            ps.executeUpdate();
                            ps.close();
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(DelWarpCommand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARP_DEL.getLocal().replaceAll("%warp", args[0]));
        } else {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
            return true;
        }
        return true;
    }

}
