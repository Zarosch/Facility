package me.velz.facility.implementations;

import lombok.Getter;
import me.velz.facility.Facility;
import org.bukkit.Bukkit;

public class Implementations {

    @Getter
    private Vault vault;
    
    @Getter
    private PlaceHolderAPI placeholderapi;

    public void hook() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            if (Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
                vault = new Vault();
                vault.addFacilityEconomy();
                vault.setupChat();
                vault.setupPermissions();
                vault.setupEconomy();
            }
        } else {
            System.out.println("[Facility] Vault was not found.");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderapi = new PlaceHolderAPI(Facility.getInstance(), "facility");
            this.placeholderapi.register();
        } else {
            System.out.println("[Facility] PlaceholderAPI was not found.");
        }
    }

    public void unhook() {
        if (vault != null) {
            vault.removeFacilityEconomy();
        }
    }

}
