package me.velz.facility.objects;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class FacilityInventoryItem {
    
    @Getter
    private final String id;
    
    @Getter
    private final ItemStack item;
    
    @Getter
    private final Integer slot;
    
    @Getter
    private final String permission;
    
    @Getter
    private final HashMap<String, ArrayList<String>> actions;

    public FacilityInventoryItem(String id, ItemStack item, Integer slot, String permission, HashMap<String, ArrayList<String>> actions) {
        this.id = id;
        this.item = item;
        this.slot = slot;
        this.permission = permission;
        this.actions = actions;
    }
    
}
