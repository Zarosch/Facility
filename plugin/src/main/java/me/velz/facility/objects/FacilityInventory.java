package me.velz.facility.objects;

import java.util.HashMap;
import lombok.Getter;

public class FacilityInventory {
    
    @Getter
    private final String displayName, id, permission;
    
    @Getter
    private final Integer size;

    @Getter
    private final HashMap<Integer, FacilityInventoryItem> items;
    
    public FacilityInventory(String displayName, String id, String permission, Integer size, HashMap<Integer, FacilityInventoryItem> items) {
        this.displayName = displayName;
        this.id = id;
        this.permission = permission;
        this.size = size;
        this.items = items;
    }
    
    
    
}
