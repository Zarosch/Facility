package me.velz.facility.objects;

import java.util.HashMap;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class FacilityInventory {
    
    @Getter
    private final String displayName, id;
    
    @Getter
    private final Integer size;

    @Getter
    private final HashMap<Integer, ItemStack> itemStack = new HashMap();
    
    public FacilityInventory(String displayName, String id, Integer size) {
        this.displayName = displayName;
        this.id = id;
        this.size = size;
    }
    
    
    
}
