package me.velz.facility.utils;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityCurrency;
import me.velz.facility.objects.FacilityKit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FileManager {
    
    private final Facility plugin;

    public FileManager(Facility plugin) {
        this.plugin = plugin;
        config = new FileBuilder(plugin.getDataFolder().getPath(), "config.yml");
        spawn = new FileBuilder(plugin.getDataFolder().getPath() + "/data", "spawn.yml");
        kits = new FileBuilder(plugin.getDataFolder().getPath() + "/data", "kits.yml");
        currencies = new FileBuilder(plugin.getDataFolder().getPath() + "/data", "currencies.yml");
        setDefaults();
        load();
    }
    
    @Getter
    @Setter
    private String permissionPrefix, newbieBroadcastMessage, newbieKit, newbieWarp, chatFormat, teleportSoundType, databaseType, databaseHost, databaseUser, databasePassword, databaseDatabase;

    @Getter
    private ArrayList<String> motds;

    @Getter
    private Integer teleportDelay, broadcastTime, chatDelay, databasePort;

    @Getter
    private boolean arenaEnabled, newbieBroadcastEnabled, broadcastEnabled, chatMention, teleportToSpawnOnJoin;

    @Getter
    @Setter
    private Location spawnLocation;

    @Getter
    private final FileBuilder config, spawn, kits, currencies;

    public final void load() {
        this.getConfig().load();
        
        //Plugin
        permissionPrefix = this.getConfig().getString("plugin.permissionPrefix");

        // Database
        databaseHost = this.getConfig().getString("database.host");
        databasePort = this.getConfig().getInt("database.port");
        databaseUser = this.getConfig().getString("database.user");
        databasePassword = this.getConfig().getString("database.password");
        databaseDatabase = this.getConfig().getString("database.database");
        databaseType = this.getConfig().getString("database.type");

        // Chat
        chatFormat = this.getConfig().getString("chat.format");
        chatMention = this.getConfig().getBoolean("chat.mention");

        // Teleport
        teleportDelay = this.getConfig().getInt("teleport.delay");
        teleportToSpawnOnJoin = this.getConfig().getBoolean("teleport.toSpawnOnJoin");

        // Server Motd
        motds = new ArrayList<>();
        this.getConfig().getConfiguration().getConfigurationSection("serverlist.motd").getKeys(false).forEach((motd) -> {
            motds.add(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("serverlist.motd." + motd)));
        });

        // Kits
        Facility.getInstance().getKits().clear();
        this.getKits().getConfiguration().getConfigurationSection("kits").getKeys(false).forEach((kit) -> {
            try {
                final ArrayList<ItemStack> items = new ArrayList<>();
                String cooldown = this.getKits().getString("kits." + kit + ".cooldown");
                String permission = this.getKits().getString("kits." + kit + ".permission");
                this.getKits().getConfiguration().getConfigurationSection("kits." + kit + ".items").getKeys(false).forEach((item) -> {
                    items.add(this.getKits().getItemStack("kits." + kit + ".items." + item));
                });
                final FacilityKit facilityKit = new FacilityKit(kit.toLowerCase(), cooldown, permission, items);
                Facility.getInstance().getKits().put(kit.toLowerCase(), facilityKit);
            } catch (NullPointerException ex) {
                System.out.println("[Facility] Error! Kit [" + kit + "] can't be loaded.");
            }
        });
        
        // Currencies
        this.getCurrencies().getConfiguration().getConfigurationSection("currencies").getKeys(false).forEach((currency) -> {
            try {
                String internalName = this.getCurrencies().getString("currencies." + currency + ".internalName");
                String displayName = this.getCurrencies().getString("currencies." + currency + ".displayName");
                String prefix = ChatColor.translateAlternateColorCodes('&', this.getCurrencies().getString("currencies." + currency + ".prefix"));
                boolean vault = this.getCurrencies().getBoolean("currencies." + currency + ".vault");
                Double startBalance = this.getCurrencies().getDouble("currencies." + currency + ".startBalance");
                plugin.getCurrencies().put(internalName, new FacilityCurrency(prefix, displayName, internalName, startBalance, vault));
            } catch (NullPointerException ex) {
                System.out.println("[Facility] Error! Currency [" + currency + "] can't be loaded.");
            }
        });
        
        // Spawn
        this.getSpawn().load();
        Bukkit.getScheduler().runTaskLater(Facility.getInstance(), () -> {
            if (spawn.getConfiguration().contains("spawn")) {
                spawnLocation = spawn.getLocation("spawn");
            }
        }, 60L);
        
        this.newbieKit = this.getConfig().getString("newbie.kit");
        this.newbieWarp = this.getConfig().getString("newbie.warp");
        this.newbieBroadcastEnabled = this.getConfig().getBoolean("newbie.broadcast.enabled");
        this.newbieBroadcastMessage = this.getConfig().getString("newbie.broadcast.message");
    }

    public final void setDefaults() {
        // Plugin
        this.getConfig().addDefault("plugin.permissionPrefix", "facility");
        
        // Database
        this.getConfig().addDefault("database.type", "SQLite");
        this.getConfig().addDefault("database.host", "localhost");
        this.getConfig().addDefault("database.port", 3306);
        this.getConfig().addDefault("database.user", "root");
        this.getConfig().addDefault("database.password", "123456");
        this.getConfig().addDefault("database.database", "Facility");
        
        // Chat
        this.getConfig().addDefault("chat.format", "%prefix%player&7:&7 %message");
        this.getConfig().addDefault("chat.mention", true);

        // Teleport
        this.getConfig().addDefault("teleport.delay", 3);
        this.getConfig().addDefault("teleport.toSpawnOnJoin", false);

        this.getConfig().addDefault("newbie.kit", "newbie");
        this.getConfig().addDefault("newbie.warp", "newbie");
        this.getConfig().addDefault("newbie.broadcast.enabled", false);
        this.getConfig().addDefault("newbie.broadcast.message", "§d%player joined the first time!");

        // Server Motd
        if (!this.getConfig().getConfiguration().contains("serverlist.motd")) {
            this.getConfig().addDefault("serverlist.motd.default", "&eFacility\n&7Makes your server better.");
            this.getConfig().addDefault("serverlist.motd.default2", "&eFacility\n&fMakes your server better.");
        }

        this.getConfig().save();

        // Default Kit
        if (!this.getKits().getConfiguration().contains("kits")) {
            this.getKits().addDefault("kits.newbie.cooldown", "30d");
            this.getKits().addDefault("kits.newbie.permission", "facility.kits.default");
            this.getKits().addDefault("kits.newbie.items.sword", new ItemBuilder().setMaterial(Material.IRON_SWORD).build());
            this.getKits().addDefault("kits.newbie.items.pickaxe", new ItemBuilder().setMaterial(Material.STONE_PICKAXE).build());
            this.getKits().addDefault("kits.newbie.items.axe", new ItemBuilder().setMaterial(Material.STONE_AXE).build());
            this.getKits().addDefault("kits.newbie.items.shovel", new ItemBuilder().setMaterial(Facility.getInstance().getVersion().getMaterial("STONE_SPADE")).build());
            this.getKits().save();
        }
        
        if(!this.getCurrencies().getConfiguration().contains("currencies")) {
            this.getCurrencies().addDefault("currencies.money.internalName", "coins");
            this.getCurrencies().addDefault("currencies.money.displayName", "Coins");
            this.getCurrencies().addDefault("currencies.money.startBalance", 0.0);
            this.getCurrencies().addDefault("currencies.money.prefix", "&8[&3Money&8] &6");
            this.getCurrencies().addDefault("currencies.money.vault", true);
            
            this.getCurrencies().addDefault("currencies.bank.internalName", "bankcoins");
            this.getCurrencies().addDefault("currencies.bank.displayName", "Coins");
            this.getCurrencies().addDefault("currencies.bank.startBalance", 0.0);
            this.getCurrencies().addDefault("currencies.bank.prefix", "&8[&3Bank&8] &6");
            this.getCurrencies().addDefault("currencies.bank.vault", false);
            this.getCurrencies().save();
        }
    }

}
