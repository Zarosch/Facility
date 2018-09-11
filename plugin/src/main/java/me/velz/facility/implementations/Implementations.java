package me.velz.facility.implementations;

import lombok.Getter;
import me.velz.facility.Facility;
import org.bukkit.Bukkit;

public class Implementations {

    @Getter
    private Vault vault;

    public void hook() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            if (Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
                vault = new Vault();
                vault.addFacilityEconomy();
                vault.setupChat();
                vault.setupPermissions();
                vault.setupEconomy();
            } else {
                System.out.println("[Facility] Vault was not found.");
            }
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            new PlaceHolderAPI(Facility.getInstance(), "facility").hook();
        } else {
            System.out.println("[Facility] PlaceHolderAPI was not found.");
        }
    }

    public void unhook() {
        if (vault != null) {
            vault.removeFacilityEconomy();
        }
    }

}
