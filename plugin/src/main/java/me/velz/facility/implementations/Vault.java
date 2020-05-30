package me.velz.facility.implementations;

import lombok.Getter;
import me.velz.facility.Facility;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class Vault {

    @Getter
    private Permission permission = null;

    @Getter
    private Economy economy = null;

    @Getter
    private Chat chat = null;

    public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Facility.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    public boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = Facility.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Facility.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public void addFacilityEconomy() {
        String vaultCurrency = "";
        for(String currency : Facility.getInstance().getCurrencies().keySet()) {
            if(Facility.getInstance().getCurrencies().get(currency).isVault()) {
                vaultCurrency = currency;
            }
        }
        if(vaultCurrency != "") {
            Bukkit.getServicesManager().register(Economy.class, new VaultEconomy(vaultCurrency), Facility.getInstance(), ServicePriority.Normal);
            System.out.println("[Facility] VaultAPI hooked into FacilityEconomy");
        } else {
            System.out.println("[Facility] VaultAPI can't hooked into FacilityEconomy because no Vault currency exists.");
        }
    }

    public void removeFacilityEconomy() {
        Bukkit.getServicesManager().unregister(Economy.class, Facility.getInstance());
        System.out.println("[Facility] VaultAPI unhooked from FacilityEconomy");
    }
    
}
