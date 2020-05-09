package me.velz.facility.objects;

import java.util.ArrayList;
import lombok.Getter;

public class FacilityCommand {
    
    @Getter
    private final String id, command, permission;
    
    @Getter
    private final ArrayList<String> actions;

    public FacilityCommand(String id, String command, String permission, ArrayList<String> actions) {
        this.id = id;
        this.command = command;
        this.permission = permission;
        this.actions = actions;
    }
    
}
