package me.velz.facility.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SQLiteDatabase implements Database {

    private Facility plugin;
    private Connection connection;

    public SQLiteDatabase(Facility plugin) {
        this.plugin = plugin;
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/Facility/database.db");
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS players (uuid TEXT PRIMARY KEY, name TEXT, money DOUBLE, token DOUBLE, playtime INTEGER, firstJoin INTEGER, lastJoin INTEGER, ban TEXT, mute TEXT)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS warps (name TEXT PRIMARY KEY, world TEXT, x DOUBLE, y REAL, z REAL, yaw REAL, pitch REAL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS homes (id INTEGER PRIMARY KEY, name TEXT, uuid TEXT, world TEXT, x REAL, y REAL, z REAL, yaw REAL, pitch REAL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS kits_cooldown (id INTEGER PRIMARY KEY, uuid TEXT, kit TEXT, expired INTEGER)");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (this.connection.isClosed()) {
                this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/Facility/database.db");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.connection;
    }

    @Override
    public void loadWarps() {
        try {
            PreparedStatement ps = this.getConnection().prepareStatement("SELECT * FROM warps");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Location loc = new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                plugin.getWarps().put(rs.getString("name"), new DatabaseWarp(rs.getString("name"), loc));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertUser(String uuid, String name) {
        try {
            PreparedStatement ps = this.getConnection().prepareStatement("INSERT INTO players (uuid, name, money, token, playtime, firstJoin, lastJoin, ban, mute) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setDouble(3, 0.0);
            ps.setDouble(4, 0.0);
            ps.setLong(5, 0);
            ps.setLong(6, System.currentTimeMillis());
            ps.setLong(7, System.currentTimeMillis());
            ps.setString(8, "OK");
            ps.setString(9, "OK");
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public DatabasePlayer getUser(String uuid) {
        String uid = uuid;
        if (uuid.length() <= 16) {
            uid = this.getUUID(uuid);
        }
        if (plugin.getPlayers().containsKey(uid)) {
            DatabasePlayer dbPlayer = plugin.getPlayers().get(uid);
            if (dbPlayer != null) {
                return dbPlayer;
            }
        }
        DatabasePlayer dbPlayer = loadUser(uid, null);
        return dbPlayer;
    }

    @Override
    public DatabasePlayer loadUser(String uuid, String name) {
        final DatabasePlayer dbPlayer = new DatabasePlayer(uuid, name);
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM players WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dbPlayer.setFirstJoin(rs.getLong("firstJoin"));
                dbPlayer.setLastJoin(rs.getLong("lastJoin"));
                dbPlayer.setMoney(rs.getDouble("money"));
                dbPlayer.setToken(rs.getDouble("token"));
                if (name != null) {
                    dbPlayer.setName(name);
                } else {
                    dbPlayer.setName(rs.getString("name"));
                }
                dbPlayer.setPlaytime(rs.getInt("playtime"));
                dbPlayer.setBan(rs.getString("ban"));
                dbPlayer.setMute(rs.getString("mute"));
                dbPlayer.setUuid(uuid);
                dbPlayer.setSuccess(true);
                if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
                    dbPlayer.setOnline(true);
                }
            }
            ps.close();
            rs.close();

            ps = connection.prepareStatement("SELECT * FROM homes WHERE uuid = ?");
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                Location loc = new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                dbPlayer.getHomes().put(rs.getString("name"), loc);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbPlayer;
    }

    @Override
    public void saveUser(String uuid, DatabasePlayer dbPlayer) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE players SET firstJoin=?, lastJoin=?, money=?, name=?, playtime=?, ban=?, mute=?, token=? WHERE uuid = ?");
            ps.setLong(1, dbPlayer.getFirstJoin());
            ps.setLong(2, dbPlayer.getLastJoin());
            ps.setDouble(3, dbPlayer.getMoney());
            ps.setString(4, dbPlayer.getName());
            ps.setInt(5, dbPlayer.getPlaytime());
            ps.setString(6, dbPlayer.getBan());
            ps.setString(7, dbPlayer.getMute());
            ps.setDouble(8, dbPlayer.getToken());
            ps.setString(9, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean issetUser(String uuid) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT uuid FROM players WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public String getUUID(String name) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT uuid FROM players WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("uuid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getName(String uuid) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT uuid FROM players WHERE uuid = ?");
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void addHome(String uuid, String name, Location loc) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setString(3, loc.getWorld().getName());
            ps.setDouble(4, loc.getX());
            ps.setDouble(5, loc.getY());
            ps.setDouble(6, loc.getZ());
            ps.setFloat(7, loc.getYaw());
            ps.setFloat(8, loc.getPitch());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteHome(String uuid, String name) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM homes WHERE uuid = ? and name = ?");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public HashMap<String, Double> moneyToplist() {
        HashMap<String, Double> toplist = new HashMap();
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM players ORDER BY money DESC LIMIT 20");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                toplist.put(rs.getString("name"), rs.getDouble("money"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toplist;
    }

    @Override
    public HashMap<String, Double> tokenToplist() {
        HashMap<String, Double> toplist = new HashMap();
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM players ORDER BY token DESC LIMIT 20");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                toplist.put(rs.getString("name"), rs.getDouble("token"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toplist;
    }

    @Override
    public boolean issetKitCooldown(String uuid, String kit) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM kits_cooldown WHERE uuid = ? and kit = ?");
            ps.setString(1, uuid);
            ps.setString(2, kit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean isKitCooldownExpired(Player player, String kit) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM kits_cooldown WHERE kit = ? and uuid = ?");
            ps.setString(1, kit);
            ps.setString(2, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long expired = rs.getLong("expired");
                if (expired >= System.currentTimeMillis()) {
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public void insertKitCooldown(String uuid, String kit, Integer expired) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO kits_cooldown (uuid, kit, expired) VALUES (?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, kit);
            ps.setLong(3, System.currentTimeMillis() + expired);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateKitCooldown(String uuid, String kit, Integer expired) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE kits_cooldown SET expired=? WHERE uuid = ?");
            ps.setLong(1, expired + System.currentTimeMillis());
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
