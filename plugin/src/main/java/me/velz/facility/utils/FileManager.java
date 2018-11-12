package me.velz.facility.utils;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityArmorstand;
import me.velz.facility.objects.FacilityKit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FileManager {

    @Getter
    @Setter
    private String newbieBroadcastMessage, newbieKit, newbieWarp, chatFormat, teleportSoundType, databaseType, databaseHost, databaseUser, databasePassword, databaseDatabase;

    @Getter
    private ArrayList<String> motds;

    @Getter
    private Integer teleportDelay, broadcastTime, chatDelay, databasePort;

    @Getter
    private boolean newbieBroadcastEnabled, broadcastEnabled;

    @Getter
    @Setter
    private Location spawnLocation;

    @Getter
    private final FileBuilder config, spawn, kits, armorstands;

    public FileManager() {
        config = new FileBuilder("plugins/Facility/", "config.yml");
        spawn = new FileBuilder("plugins/Facility/data/", "spawn.yml");
        kits = new FileBuilder("plugins/Facility/content/", "kits.yml");
        armorstands = new FileBuilder("plugins/Facility/content/", "armorstands.yml");
        setDefaults();
        load();
    }

    public final void load() {
        this.getConfig().load();

        // Database
        databaseHost = this.getConfig().getString("database.host");
        databasePort = this.getConfig().getInt("database.port");
        databaseUser = this.getConfig().getString("database.user");
        databasePassword = this.getConfig().getString("database.password");
        databaseDatabase = this.getConfig().getString("database.database");
        databaseType = this.getConfig().getString("database.type");

        // Chat
        chatFormat = this.getConfig().getString("chat.format");

        // Teleport
        teleportDelay = this.getConfig().getInt("teleport.delay");

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
                System.out.println("[Facility] Error! Kit [" + kit + "] cannot be loaded.");
            }
        });

        // Armorstands
        Facility.getInstance().getArmorstands().clear();
        this.getArmorstands().getConfiguration().getConfigurationSection("armorstands").getKeys(false).forEach((armorstand) -> {
            try {
                final String permission = this.getArmorstands().getString("armorstands." + armorstand + ".permission");
                final String name = ChatColor.translateAlternateColorCodes('&', this.getArmorstands().getString("armorstands." + armorstand + ".displayName"));
                final String world = this.getArmorstands().getString("armorstands." + armorstand + ".world");
                final ArrayList<String> playerCommands = this.getArmorstands().getStringListAsArrayList("armorstands." + armorstand + ".commands.player");
                final ArrayList<String> consoleCommands = this.getArmorstands().getStringListAsArrayList("armorstands." + armorstand + ".commands.console");
                final FacilityArmorstand facilityArmorstand = new FacilityArmorstand(world, name, permission, playerCommands, consoleCommands);
                Facility.getInstance().getArmorstands().put(name, facilityArmorstand);
            } catch (NullPointerException ex) {
                System.out.println("[Facility] Error! Armorstand [" + armorstand + "] cannot be loaded.");
            }
        });

        // Spawn
        this.getSpawn().load();
        Bukkit.getScheduler().runTaskLater(Facility.getInstance(), () -> {
            if (spawn.getConfiguration().contains("spawn")) {
                spawnLocation = spawn.getLocation("spawn");
            }
        }, 60L);
        
        Facility.getInstance().getBroadcasts().load();

        this.newbieKit = this.getConfig().getString("newbie.kit");
        this.newbieWarp = this.getConfig().getString("newbie.warp");
        this.newbieBroadcastEnabled = this.getConfig().getBoolean("newbie.broadcast.enabled");
        this.newbieBroadcastMessage = this.getConfig().getString("newbie.broadcast.message");
    }

    public final void setDefaults() {
        // Database
        this.getConfig().addDefault("database.type", "SQLite");
        this.getConfig().addDefault("database.host", "localhost");
        this.getConfig().addDefault("database.port", 3306);
        this.getConfig().addDefault("database.user", "root");
        this.getConfig().addDefault("database.password", "123456");
        this.getConfig().addDefault("database.database", "Facility");

        // Chat
        this.getConfig().addDefault("chat.format", "%prefix%player &7>&7 %message");

        // Teleport
        this.getConfig().addDefault("teleport.delay", 3);

        this.getConfig().addDefault("newbie.kit", "newbie");
        this.getConfig().addDefault("newbie.warp", "newbie");
        this.getConfig().addDefault("newbie.broadcast.enabled", false);
        this.getConfig().addDefault("newbie.broadcast.message", "§dWir haben einen neuen Mitspieler! Willkommen %player!");

        // Server Motd
        if (!this.getConfig().getConfiguration().contains("serverlist.motd")) {
            this.getConfig().addDefault("serverlist.motd.default", "&eFacility\n&eAlles in einem Plugin.");
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

        if (!this.getArmorstands().getConfiguration().contains("armorstands")) {
            this.getArmorstands().addDefault("armorstands.default.displayName", "&eTest");
            this.getArmorstands().addDefault("armorstands.default.permission", "armorstand.test");
            this.getArmorstands().addDefault("armorstands.default.commands.player", new String[]{
                "spawn"
            });
            this.getArmorstands().addDefault("armorstands.default.commands.console", new String[]{
                "broadcast %player has used the Armorstand"
            });
            this.getArmorstands().save();
        }

        Facility.getInstance().getBroadcasts().setdefaults();
    }

}
