package me.velz.facility.arenas;

import java.util.ArrayList;
import lombok.Getter;
import me.velz.facility.Facility;

public class FacilityArenaMode {
    
    @Getter
    private String id;
    
    @Getter
    private ArrayList<String> settings;

    public FacilityArenaMode(String id) {
        this.id = id;
        load();
    }
    
    public void load() {
        settings = Facility.getInstance().getFileManager().getArenas().getStringListAsArrayList("modes." + id + ".settings");
    }
    
}
