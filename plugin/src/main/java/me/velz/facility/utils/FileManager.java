package me.velz.facility.utils;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityKit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FileManager {

    @Getter
    @Setter
    private String chatFormat, teleportSoundType;

    @Getter
    private ArrayList<String> motds;

    @Getter
    private Integer teleportDelay;

    @Getter
    private boolean teleportSoundEnabled;

    @Getter
    @Setter
    private Location spawnLocation;

    @Getter
    private final FileBuilder config, spawn, kits;

    public FileManager() {
        config = new FileBuilder("plugins/Facility/", "config.yml");
        spawn = new FileBuilder("plugins/Facility/data/", "spawn.yml");
        kits = new FileBuilder("plugins/Facility/", "kits.yml");
        setDefaults();
        load();
    }

    public final void load() {
        this.getConfig().load();

        // Database
        Facility.getInstance().getMysqlDatabase().setServerName(this.getConfig().getString("database.host"));
        Facility.getInstance().getMysqlDatabase().setPort(this.getConfig().getInt("database.port"));
        Facility.getInstance().getMysqlDatabase().setUser(this.getConfig().getString("database.user"));
        Facility.getInstance().getMysqlDatabase().setPassword(this.getConfig().getString("database.password"));
        Facility.getInstance().getMysqlDatabase().setDatabaseName(this.getConfig().getString("database.database"));
        Facility.getInstance().getMysqlDatabase().setPrefix(this.getConfig().getString("database.prefix"));

        // Chat
        chatFormat = this.getConfig().getString("chat.format");

        // Teleport
        teleportDelay = this.getConfig().getInt("teleport.delay");
        teleportSoundEnabled = this.getConfig().getBoolean("teleport.sound.enabled");
        teleportSoundType = this.getConfig().getString("teleport.sound.type");

        // Server Motd
        motds = new ArrayList<>();
        this.getConfig().getConfiguration().getConfigurationSection("serverlist.motd").getKeys(false).forEach((motd) -> {
            motds.add(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("serverlist.motd." + motd)));
        });

        // Kits
        for (String kit : this.getKits().getConfiguration().getConfigurationSection("kits").getKeys(false)) {
            try {
                final ArrayList<ItemStack> items = new ArrayList<>();
                String cooldown = this.getKits().getString("kits." + kit + ".cooldown");
                String permission = this.getKits().getString("kits." + kit + ".permission");
                for (String item : this.getKits().getConfiguration().getConfigurationSection("kits." + kit + ".items").getKeys(false)) {
                    items.add(this.getKits().getItemStack("kits." + kit + ".items." + item));
                }
                final FacilityKit facilityKit = new FacilityKit(kit.toLowerCase(), cooldown, permission, items);
                Facility.getInstance().getKits().put(kit.toLowerCase(), facilityKit);
            } catch (NullPointerException ex) {
                System.out.println("[Facility] Error! Kit [" + kit + "] cannot be loaded.");
            }
        }

        // Spawn
        this.getSpawn().load();
        Bukkit.getScheduler().runTaskLater(Facility.getInstance(), () -> {
            if (spawn.getConfiguration().contains("spawn")) {
                spawnLocation = spawn.getLocation("spawn");
            }
        }, 60L);
    }

    public final void setDefaults() {
        // Database
        this.getConfig().addDefault("database.host", "localhost");
        this.getConfig().addDefault("database.port", 3306);
        this.getConfig().addDefault("database.user", "root");
        this.getConfig().addDefault("database.password", "123456");
        this.getConfig().addDefault("database.database", "Facility");
        this.getConfig().addDefault("database.prefix", "facility_");

        // Chat
        this.getConfig().addDefault("chat.format", "%prefix%player &7>&7 %message");

        // Teleport
        this.getConfig().addDefault("teleport.delay", 3);
        this.getConfig().addDefault("teleport.sound.enabled", false);
        this.getConfig().addDefault("teleport.sound.type", "ENTITY_ENDERMEN_TELEPORT");

        // Server Motd
        if (!this.getConfig().getConfiguration().contains("serverlist.motd")) {
            this.getConfig().addDefault("serverlist.motd.default", "&eFacility\n&eAlles in einem Plugin.");
        }

        this.getConfig().save();

        // Default Kit
        if (!this.getKits().getConfiguration().contains("kits")) {
            this.getKits().addDefault("kits.default.cooldown", "30d");
            this.getKits().addDefault("kits.default.permission", "facility.kits.default");
            this.getKits().addDefault("kits.default.items.sword", new ItemBuilder().setMaterial(Material.IRON_SWORD).build());
            this.getKits().addDefault("kits.default.items.pickaxe", new ItemBuilder().setMaterial(Material.STONE_PICKAXE).build());
            this.getKits().addDefault("kits.default.items.axe", new ItemBuilder().setMaterial(Material.STONE_AXE).build());
            this.getKits().addDefault("kits.default.items.shovel", new ItemBuilder().setMaterial(Facility.getInstance().getVersion().getMaterial("STONE_SPADE")).build());
            this.getKits().save();
        }
    }

}
