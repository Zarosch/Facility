package me.velz.facility.objects;

import java.util.ArrayList;
import lombok.Getter;

public class FacilityArmorstand {
    
    @Getter
    public String world, name, permission;
    
    @Getter
    private final ArrayList<String> actions;

    public FacilityArmorstand(String world, String name, String permission, ArrayList<String> actions) {
        this.world = world;
        this.name = name;
        this.permission = permission;
        this.actions = actions;
    }
    
}
