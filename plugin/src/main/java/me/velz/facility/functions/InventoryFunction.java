package me.velz.facility.functions;

import java.util.HashMap;
import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityInventory;
import me.velz.facility.utils.FileBuilder;
import me.velz.facility.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryFunction implements Function, Listener {

    @Getter
    private FileBuilder config;
    
    @Getter
    private final HashMap<String, FacilityInventory> inventories = new HashMap();

    @Override
    public void onEnable() {
        config = new FileBuilder(Facility.getInstance().getDataFolder() + "/functions", "inventories.function.yml");
        Bukkit.getPluginManager().registerEvents(this, Facility.getInstance());
        this.onReload();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {
        this.getConfig().load();

        if (!config.getConfiguration().contains("inventories")) {
            config.addDefault("inventories.test.displayName", "&6Inventory");
            config.addDefault("inventories.test.permission", "facility.inventory.test");
            config.addDefault("inventories.test.size", 18);
            config.addDefault("inventories.test.items.item.item", new ItemBuilder().setDisplayName("§6Test").setMaterial(Material.STONE).build());
            config.addDefault("inventories.test.items.item.slot", 1);
            config.addDefault("inventories.test.items.item.permission", "facility.inventory.test");
            config.addDefault("inventories.test.items.item.actions", new String[]{
                "message>&eTest"
            });
        }

        this.getConfig().save();
    }

    @Override
    public void onSchedule() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        
    }

}
