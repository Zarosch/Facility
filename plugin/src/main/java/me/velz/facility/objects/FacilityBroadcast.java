package me.velz.facility.objects;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class FacilityBroadcast {
    
    @Getter
    private final String name;
    
    @Getter
    private final List<String> message;

    @Getter @Setter
    private Boolean broadcasted = false;
    
    public FacilityBroadcast(String name, List<String> message) {
        this.name = name;
        this.message = message;
    }
    
}
