package me.velz.facility.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MySQLDatabase implements Database {

    private final Facility plugin;
    private Connection connection;

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public MySQLDatabase(Facility plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + plugin.getFileManager().getDatabaseHost() + ":" + plugin.getFileManager().getDatabasePort() + "/" + plugin.getFileManager().getDatabaseDatabase() + "?autoReconnect=true", plugin.getFileManager().getDatabaseUser(), plugin.getFileManager().getDatabasePassword());
                if (this.connection != null && !this.connection.isClosed()) {
                    System.out.println("[Facility] Connected successfully to MySQL database.");
                    Statement statement = this.connection.createStatement();
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(36), name VARCHAR(16), groupname VARCHAR(16), playtime BIGINT, firstJoin BIGINT, lastJoin BIGINT, ban VARCHAR(100), mute VARCHAR(100), UNIQUE KEY (uuid))");
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS players_currencies (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(36), currency VARCHAR(255), balance DOUBLE, UNIQUE KEY (id))");
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS players_meta (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(36), metakey VARCHAR(255), metavalue VARCHAR(255), UNIQUE KEY (id))");
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS warps (name VARCHAR(32), world VARCHAR(32), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, UNIQUE KEY (name))");
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS homes (id int NOT NULL AUTO_INCREMENT, name VARCHAR(32), uuid VARCHAR(36), world VARCHAR(32), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, UNIQUE KEY (id))");
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS kits_cooldown (id int NOT NULL AUTO_INCREMENT, uuid VARCHAR(36), kit VARCHAR(100), expired BIGINT, UNIQUE KEY (id))");
                    loadWarps();
                } else {
                    System.out.println("[Facility] Soory, your database credentials are unvalid.");
                    System.out.println("[Facility] Facility can only used with a valid database connection, plugin will be disabled.");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            } catch (SQLException ex) {
                Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getConnection()">
    @Override
    public Connection getConnection() {
        try {
            if (this.connection.isClosed()) {
                this.connection = DriverManager.getConnection("jdbc:mysql://" + plugin.getFileManager().getDatabaseHost() + ":" + plugin.getFileManager().getDatabasePort() + "/" + plugin.getFileManager().getDatabaseDatabase() + "?autoReconnect=true", plugin.getFileManager().getDatabaseUser(), plugin.getFileManager().getDatabasePassword());
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.connection;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadWarps()">
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
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="insertUser(uuid, name)">
    @Override
    public void insertUser(String uuid, String name) {
        try {
            String group = "";
            if (plugin.getImplementations().getVault() != null) {
                if (Bukkit.getPlayer(name) != null) {
                    group = plugin.getImplementations().getVault().getPermission().getPlayerGroups(Bukkit.getPlayer(name))[0];
                }
            }

            PreparedStatement ps = this.getConnection().prepareStatement("INSERT INTO players (uuid, name, groupname, playtime, firstJoin, lastJoin, ban, mute) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.setString(3, group);
            ps.setLong(4, 0);
            ps.setLong(5, System.currentTimeMillis());
            ps.setLong(6, System.currentTimeMillis());
            ps.setString(7, "OK");
            ps.setString(8, "OK");
            ps.executeUpdate();

            String values = "";
            for (String currency : plugin.getCurrencies().keySet()) {
                values = values + "(?,?,?),";
            }
            values = values.substring(0, values.length() - 1);

            PreparedStatement insertCurrencies = this.getConnection().prepareStatement("INSERT INTO players_currencies (uuid, currency, balance) VALUES " + values);
            int count = 1;
            for (String currency : plugin.getCurrencies().keySet()) {
                insertCurrencies.setString(count, uuid);
                count++;
                insertCurrencies.setString(count, currency);
                count++;
                insertCurrencies.setDouble(count, plugin.getCurrencies().get(currency).getStartBalance());
                count++;
            }
            insertCurrencies.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getUser(uuid)">
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadUser(uuid, name)">
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
                if (name != null) {
                    dbPlayer.setName(name);
                } else {
                    dbPlayer.setName(rs.getString("name"));
                }
                dbPlayer.setPlaytime(rs.getInt("playtime"));
                dbPlayer.setBan(rs.getString("ban"));
                dbPlayer.setMute(rs.getString("mute"));
                dbPlayer.setGroup(rs.getString("groupname"));
                dbPlayer.setUuid(uuid);
                dbPlayer.setSuccess(true);
                if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
                    dbPlayer.setOnline(true);
                }
            }
            ps.close();
            rs.close();

            ps = getConnection().prepareStatement("SELECT * FROM homes WHERE uuid = ?");
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                Location loc = new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                dbPlayer.getHomes().put(rs.getString("name"), loc);
            }
            ps.close();
            rs.close();

            ps = getConnection().prepareStatement("SELECT * FROM players_currencies WHERE uuid = ?");
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                dbPlayer.getCurrencies().put(rs.getString("currency"), rs.getDouble("balance"));
            }
            ps.close();
            rs.close();

            ArrayList<String> insertNewCurrencies = new ArrayList();
            
            for (String currency : plugin.getCurrencies().keySet()) {
                if (!dbPlayer.getCurrencies().containsKey(currency)) {
                    insertNewCurrencies.add(currency);
                    dbPlayer.getCurrencies().put(currency, plugin.getCurrencies().get(currency).getStartBalance());
                }
            }
            
            if(!insertNewCurrencies.isEmpty()) {
                String values = "";
                for (String currency : insertNewCurrencies) {
                    values = values + "(?,?,?),";
                }
                values = values.substring(0, values.length() - 1);

                PreparedStatement insertCurrencies = this.getConnection().prepareStatement("INSERT INTO players_currencies (uuid, currency, balance) VALUES " + values);
                int count = 1;
                for (String currency : insertNewCurrencies) {
                    insertCurrencies.setString(count, uuid);
                    count++;
                    insertCurrencies.setString(count, currency);
                    count++;
                    insertCurrencies.setDouble(count, plugin.getCurrencies().get(currency).getStartBalance());
                    count++;
                }
                insertCurrencies.executeUpdate();
            }
            
            ps = getConnection().prepareStatement("SELECT * FROM players_meta WHERE uuid = ?");
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            while (rs.next()) {
                dbPlayer.getMeta().put(rs.getString("metakey"), rs.getString("metavalue"));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbPlayer;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="saveUser(uuid, dbPlayer)">
    @Override
    public void saveUser(String uuid, DatabasePlayer dbPlayer) {
        try {
            String group = dbPlayer.getGroup();
            if (plugin.getImplementations().getVault() != null) {
                if (Bukkit.getPlayer(dbPlayer.getName()) != null) {
                    group = plugin.getImplementations().getVault().getPermission().getPlayerGroups(Bukkit.getPlayer(dbPlayer.getName()))[0];
                }
            }

            PreparedStatement ps = getConnection().prepareStatement("UPDATE players SET firstJoin=?, lastJoin=?, name=?, groupname=?, playtime=?, ban=?, mute=? WHERE uuid = ?");
            ps.setLong(1, dbPlayer.getFirstJoin());
            ps.setLong(2, dbPlayer.getLastJoin());
            ps.setString(3, dbPlayer.getName());
            ps.setString(4, group);
            ps.setInt(5, dbPlayer.getPlaytime());
            ps.setString(6, dbPlayer.getBan());
            ps.setString(7, dbPlayer.getMute());
            ps.setString(8, uuid);
            ps.executeUpdate();
            ps.close();

            String query = "UPDATE players_currencies SET balance = (case";
            for (String currency : plugin.getCurrencies().keySet()) {
                query = query + " when currency = ? then ?";
            }
            query = query + " end) WHERE uuid = ?";
            PreparedStatement insertCurrencies = getConnection().prepareStatement(query);
            int count = 1;
            for (String currency : dbPlayer.getCurrencies().keySet()) {
                insertCurrencies.setString(count, currency);
                count++;
                insertCurrencies.setDouble(count, dbPlayer.getCurrencies().get(currency));
                count++;
            }
            insertCurrencies.setString(count, uuid);
            insertCurrencies.executeUpdate();
            insertCurrencies.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="issetUser(uuid)">
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
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getUUID(name)">
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
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getName(uuid)">
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
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addHome(uuid, name, loc)">
    @Override
    public void addHome(String uuid, String name, Location loc) {
        try {
            String query = "SELECT * from homes WHERE name = ? and uuid = ?";
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps = getConnection().prepareStatement("UPDATE homes SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE name = ? and uuid = ?");
                ps.setString(1, loc.getWorld().getName());
                ps.setDouble(2, loc.getX());
                ps.setDouble(3, loc.getY());
                ps.setDouble(4, loc.getZ());
                ps.setFloat(5, loc.getYaw());
                ps.setFloat(6, loc.getPitch());
                ps.setString(7, uuid);
                ps.setString(8, name);
                ps.executeUpdate();
                ps.close();
            } else {
                ps = getConnection().prepareStatement("INSERT INTO homes (uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deleteHome(uuid, name)">
    @Override
    public void deleteHome(String uuid, String name) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM homes WHERE uuid = ? and name = ?");
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="currencyToplist()">
    @Override
    public HashMap<Integer, HashMap<String, Double>> currencyToplist(String currency) {
        HashMap<Integer, HashMap<String, Double>> toplist = new HashMap();
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT players.name, players_currencies.balance, players_currencies.currency FROM players_currencies INNER JOIN players ON players_currencies.uuid = players.uuid WHERE players_currencies.currency=? ORDER BY players_currencies.balance DESC LIMIT 20");
            ps.setString(1, currency);
            ResultSet rs = ps.executeQuery();
            Integer i = 1;
            while (rs.next()) {
                HashMap<String, Double> entry = new HashMap<>();
                entry.put(rs.getString("players.name"), rs.getDouble("players_currencies.balance"));
                toplist.put(i, entry);
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toplist;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="issetKitCooldown(uuid, kit)">
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
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isKitCooldownExpired(player, kit)">
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
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="insertKitCooldown(uuid, kit, expired)">
    @Override
    public void insertKitCooldown(String uuid, String kit, Integer expired) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO kits_cooldown (uuid, kit, expired) VALUES (?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, kit);
            ps.setLong(3, System.currentTimeMillis() + expired);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="updateKitCooldown(uuid, kit, expired)">
    @Override
    public void updateKitCooldown(String uuid, String kit, Integer expired) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE kits_cooldown SET expired=? WHERE uuid = ?");
            ps.setLong(1, expired + System.currentTimeMillis());
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addWarp(name, loc)">
    @Override
    public void addWarp(String name, Location loc) {
        PreparedStatement ps;
        ResultSet rs;
        try {
            String query = "SELECT * from warps WHERE name = ?";
            ps = this.connection.prepareStatement(query);
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                query = "UPDATE warps SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE name = ?";
                ps = connection.prepareStatement(query);
                ps.setString(1, loc.getWorld().getName());
                ps.setDouble(2, loc.getX());
                ps.setDouble(3, loc.getY());
                ps.setDouble(4, loc.getZ());
                ps.setFloat(5, loc.getYaw());
                ps.setFloat(6, loc.getPitch());
                ps.setString(7, name);
                ps.executeUpdate();
                ps.close();
            } else {
                query = "INSERT INTO warps (name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = connection.prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, loc.getWorld().getName());
                ps.setDouble(3, loc.getX());
                ps.setDouble(4, loc.getY());
                ps.setDouble(5, loc.getZ());
                ps.setFloat(6, loc.getYaw());
                ps.setFloat(7, loc.getPitch());
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deleteWarp(name)">
    @Override
    public void deleteWarp(String name) {
        try {
            String query = "SELECT * FROM warps WHERE name = ?";
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                query = "DELETE FROM warps WHERE name = ?";
                ps = this.connection.prepareStatement(query);
                ps.setString(1, name);
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addMetadata(uuid, key, value)">
    @Override
    public void addMetadata(String uuid, String key, String value) {
        PreparedStatement ps;
        ResultSet rs;
        try {
            String query = "SELECT * from players_meta WHERE key = ? AND uuid = ?";
            ps = this.connection.prepareStatement(query);
            ps.setString(1, key);
            ps.setString(2, uuid);
            rs = ps.executeQuery();
            if (rs.next()) {
                query = "UPDATE players_meta SET value = ? WHERE key = ? AND uuid = ?";
                ps = connection.prepareStatement(query);
                ps.setString(1, value);
                ps.setString(2, key);
                ps.setString(3, uuid);
                ps.executeUpdate();
                ps.close();
            } else {
                query = "INSERT INTO players_meta (uuid,key,value) VALUES (?, ?, ?)";
                ps = connection.prepareStatement(query);
                ps.setString(1, uuid);
                ps.setString(2, key);
                ps.setString(3, value);
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="removeMetadata(uuid, key)">
    @Override
    public void removeMetadata(String uuid, String key) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("DELETE FROM players_meta WHERE uuid = ? and key = ?");
            ps.setString(1, uuid);
            ps.setString(2, key);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getMetadata(uuid, key)">
    @Override
    public String getMetadata(String uuid, String key) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM players_meta WHERE uuid = ? and key = ?");
            ps.setString(1, uuid);
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("value");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    //</editor-fold>

}
