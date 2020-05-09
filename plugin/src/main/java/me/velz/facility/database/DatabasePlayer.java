package me.velz.facility.database;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.Facility;
import org.bukkit.Location;

public class DatabasePlayer {

    @Getter
    @Setter
    private String uuid, name, ban, mute, group;
    
    @Getter
    @Setter
    private double money, token;
    
    @Getter
    @Setter
    private int playtime;
    
    @Getter
    @Setter
    private Long firstJoin, lastJoin;
    
    @Getter
    @Setter
    private boolean success = false, online = false;
    
    @Getter
    private final HashMap<String, Location> homes = new HashMap<>();
    
    @Getter
    private final HashMap<String, String> meta = new HashMap<>();

    public DatabasePlayer(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public final void addHome(String name, Location loc) {
        Facility.getInstance().getDatabase().addHome(uuid, name, loc);
        homes.put(name, loc);
    }

    public final void deleteHome(String name) {
        Facility.getInstance().getDatabase().deleteHome(uuid, name);
        homes.remove(name);
    }

    public void save() {
        Facility.getInstance().getDatabase().saveUser(uuid, this);
    }

}
