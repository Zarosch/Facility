package me.velz.facility.objects;

import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class FacilityInventoryItem {
    
    @Getter
    private final ItemStack item;
    
    @Getter
    private final Integer slot;
    
    @Getter
    private final String permission;
    
    @Getter
    private final ArrayList<String> actions = new ArrayList<>();

    public FacilityInventoryItem(ItemStack item, Integer slot, String permission) {
        this.item = item;
        this.slot = slot;
        this.permission = permission;
    }
    
}
