package me.velz.facility.functions;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityArmorstand;
import me.velz.facility.utils.Actions;
import me.velz.facility.utils.FileBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ArmorstandFunction implements Function, Listener {

    @Getter
    private final HashMap<String, FacilityArmorstand> armorstands = new HashMap();

    @Getter
    private FileBuilder config;

    @Override
    public void onEnable() {
        config = new FileBuilder(Facility.getInstance().getDataFolder().getPath() + "/functions", "armorstand.function.yml");
        Bukkit.getPluginManager().registerEvents(this, Facility.getInstance());
        this.onReload();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onReload() {
        this.getArmorstands().clear();
        if (!this.getConfig().getConfiguration().contains("armorstands")) {
            this.getConfig().addDefault("armorstands.default.name", "&eTest");
            this.getConfig().addDefault("armorstands.default.permission", "armorstand.test");
            this.getConfig().addDefault("armorstands.default.actions", new String[]{
                "message>&eHihii ein Armorstand!"
            });
            this.getConfig().save();
        }
        this.getConfig().getConfiguration().getConfigurationSection("armorstands").getKeys(false).forEach((armorstand) -> {
            try {
                final String permission = this.getConfig().getString("armorstands." + armorstand + ".permission");
                final String name = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("armorstands." + armorstand + ".name"));
                final String world = this.getConfig().getString("armorstands." + armorstand + ".world");
                final ArrayList<String> actions = this.getConfig().getStringListAsArrayList("armorstands." + armorstand + ".actions");
                final FacilityArmorstand facilityArmorstand = new FacilityArmorstand(world, name, permission, actions);
                this.getArmorstands().put(name, facilityArmorstand);
            } catch (NullPointerException ex) {
                System.out.println("[Facility] Error! Armorstand [" + armorstand + "] cannot be loaded.");
            }
        });
    }

    @Override
    public void onSchedule() {
    }

    @EventHandler
    public void onArmorstand(PlayerArmorStandManipulateEvent event) {
        if (event.getRightClicked().getCustomName() == null) {
            return;
        }
        for (FacilityArmorstand armorStand : this.getArmorstands().values()) {
            if (armorStand.getName().equalsIgnoreCase(event.getRightClicked().getName())) {
                if (event.getPlayer().hasPermission(armorStand.getPermission())) {
                    for (String action : armorStand.getActions()) {
                        Actions.run(event.getPlayer(), action);
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onArmorstand(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            if (event.getRightClicked().getCustomName() == null) {
                return;
            }
            for (FacilityArmorstand armorStand : this.getArmorstands().values()) {
                if (armorStand.getName().equalsIgnoreCase(event.getRightClicked().getName())) {
                    if (event.getPlayer().hasPermission(armorStand.getPermission())) {
                        for (String action : armorStand.getActions()) {
                            Actions.run(event.getPlayer(), action);
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
