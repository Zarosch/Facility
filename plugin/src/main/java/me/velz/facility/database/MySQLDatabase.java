package me.velz.facility.database;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.Facility;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MySQLDatabase {

    private final Facility plugin;

    public MySQLDatabase(Facility plugin) {
        this.plugin = plugin;
    }

    @Getter
    private HikariDataSource hikari;

    @Setter
    private String serverName, databaseName, user, password;

    @Getter
    @Setter
    private String prefix;

    @Setter
    private Integer port;

    public final void connect() {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", serverName);
        hikari.addDataSourceProperty("port", port.toString());
        hikari.addDataSourceProperty("databaseName", databaseName);
        hikari.addDataSourceProperty("user", user);
        hikari.addDataSourceProperty("useSSL", "false");
        hikari.addDataSourceProperty("password", password);
        hikari.setMaxLifetime(30000);
        createTables();
        loadWarps();
    }

    public final void createTables() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "players (uuid VARCHAR(36), name VARCHAR(16), permissionGroup VARCHAR(25), money DOUBLE, token DOUBLE, playtime BIGINT, firstJoin BIGINT, lastJoin BIGINT, ban VARCHAR(100), mute VARCHAR(100), prefix VARCHAR(100), suffix VARCHAR(100), UNIQUE KEY (uuid))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "warps (name VARCHAR(32), world VARCHAR(32), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, UNIQUE KEY (name))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "homes (id int NOT NULL AUTO_INCREMENT, name VARCHAR(32), uuid VARCHAR(36), world VARCHAR(32), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, UNIQUE KEY (id))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + prefix + "permissions (id int NOT NULL AUTO_INCREMENT, uuid VARCHAR(36), permission VARCHAR(100), UNIQUE KEY (id))");
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final void loadWarps() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = hikari.getConnection();
            String query = "SELECT * FROM " + prefix + "warps";
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Location loc = new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                plugin.getWarps().put(rs.getString("name"), new DatabaseWarp(rs.getString("name"), loc));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();

                }
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final void insertUser(String uuid, String name) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO " + this.getPrefix() + "players (uuid, name, money, token, playtime, firstJoin, lastJoin, ban, mute) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
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
            Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final DatabasePlayer getUser(String uuid) {
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

    public final DatabasePlayer loadUser(String uuid, String name) {
        final DatabasePlayer dbPlayer = new DatabasePlayer(uuid, name);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM " + this.getPrefix() + "players WHERE uuid = ?";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
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

            query = "SELECT * FROM " + this.getPrefix() + "homes WHERE uuid = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                Location loc = new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                dbPlayer.getHomes().put(rs.getString("name"), loc);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dbPlayer;
    }

    public final void saveUser(String uuid, DatabasePlayer dbPlayer) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            String query = "UPDATE " + this.getPrefix() + "players SET firstJoin=?, lastJoin=?, money=?, name=?, playtime=?, ban=?, mute=? WHERE uuid = ?";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            ps.setLong(1, dbPlayer.getFirstJoin());
            ps.setLong(2, dbPlayer.getLastJoin());
            ps.setDouble(3, dbPlayer.getMoney());
            ps.setString(4, dbPlayer.getName());
            ps.setInt(5, dbPlayer.getPlaytime());
            ps.setString(6, dbPlayer.getBan());
            ps.setString(7, dbPlayer.getMute());
            ps.setString(8, uuid);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final boolean issetUser(String uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT uuid FROM " + this.getPrefix() + "players WHERE uuid = ?";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public final String getUUID(String name) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT uuid FROM " + this.getPrefix() + "players WHERE name = ?";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("uuid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public final String getName(String uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT uuid FROM " + this.getPrefix() + "players WHERE uuid = ?";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public final void addHome(String uuid, String name, Location loc) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO " + this.getPrefix() + "homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setString(3, loc.getWorld().getName());
            ps.setDouble(4, loc.getX());
            ps.setDouble(5, loc.getY());
            ps.setDouble(6, loc.getZ());
            ps.setFloat(7, loc.getYaw());
            ps.setFloat(8, loc.getPitch());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DatabasePlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final void deleteHome(String uuid, String name) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            String query = "DELETE FROM " + this.getPrefix() + "homes WHERE uuid = ? and name = ?";
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final HashMap<String, Double> moneyToplist() {
        HashMap<String, Double> toplist = new HashMap();
        final String query = "SELECT * FROM " + this.getPrefix() + "players ORDER BY money DESC LIMIT 20";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                toplist.put(rs.getString("name"), rs.getDouble("money"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return toplist;
    }

    public final HashMap<String, Double> tokenToplist() {
        HashMap<String, Double> toplist = new HashMap();
        final String query = "SELECT * FROM " + this.getPrefix() + "players ORDER BY token DESC LIMIT 20";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = this.getHikari().getConnection();
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                toplist.put(rs.getString("name"), rs.getDouble("token"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return toplist;
    }

}
