package me.velz.facility.database;

import lombok.Getter;
import org.bukkit.Location;

public class DatabaseWarp {

    private final String name;
    @Getter private final Location loc;
    
    public DatabaseWarp(String name, Location loc) {
        this.name = name;
        this.loc = loc;
    }
    
    
    
}
