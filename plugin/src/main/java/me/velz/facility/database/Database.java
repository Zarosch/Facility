package me.velz.facility.database;

import java.sql.Connection;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Database {
    
    public Connection getConnection();
    public void loadWarps();
    
    public void insertUser(String uuid, String name);
    public DatabasePlayer getUser(String uuid);
    public DatabasePlayer loadUser(String uuid, String name);
    public void saveUser(String uuid, DatabasePlayer dbPlayer);
    public boolean issetUser(String uuid);
    public String getUUID(String name);
    public String getName(String uuid);
    
    public void addMetadata(String uuid, String key, String value);
    public void removeMetadata(String uuid, String key);
    public String getMetadata(String uuid, String key);
    
    public void addHome(String uuid, String name, Location loc);
    public void deleteHome(String uuid, String name);
    
    public HashMap<String, Double> moneyToplist();
    public HashMap<String, Double> tokenToplist();
    
    public boolean issetKitCooldown(String uuid, String kit);
    public boolean isKitCooldownExpired(Player player, String kit);
    public void insertKitCooldown(String uuid, String kit, Integer expired);
    public void updateKitCooldown(String uuid, String kit, Integer expired);
    
    public void addWarp(String name, Location loc);
    public void deleteWarp(String name);
    
}
