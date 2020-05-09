package me.velz.facility.arenas;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class FacilityArena {

    @Getter
    private String id;
    
    @Getter @Setter
    private FacilityArenaMode mode;
    
    @Getter
    private HashMap<String, Location> locations;
    
    @Getter
    private ArrayList<String> settings;
    
    @Getter
    private final ArrayList<Player> players;
    
    public FacilityArena(String id) {
        players = new ArrayList<>();
        load();
    }
    
    public void join(Player player) {
        if(!this.locations.containsKey("spawn") || mode == null || this.settings.contains("locked")) {
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_JOIN_FAILED.getLocal());
            return;
        }
        players.add(player);
        player.setMetadata("arena", new FixedMetadataValue(Facility.getInstance(), id));
        Facility.getInstance().getArenaManager().getPlayerSaveUtil().save(player);
        Facility.getInstance().getArenaManager().getPlayerSaveUtil().reset(player);
        if(this.locations.containsKey("spawn")) {
            player.teleport(this.locations.get("spawn"));
        }
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_JOIN_JOINED.getLocal().replaceAll("%arena", id));
    }
    
    public void leave(Player player) {
        players.remove(player);
        player.removeMetadata("arena", Facility.getInstance());
        Facility.getInstance().getArenaManager().getPlayerSaveUtil().reset(player);
        Facility.getInstance().getArenaManager().getPlayerSaveUtil().load(player);
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_LEAVE.getLocal().replaceAll("%arena", id));
    }
    
    public void load() {
        if(Facility.getInstance().getFileManager().getArenas().getConfiguration().contains("arenas." + id + ".mode")) {
            if(Facility.getInstance().getArenaManager().getModes().containsKey(Facility.getInstance().getFileManager().getArenas().getString("arenas." + id + ".mode"))) {
                mode = Facility.getInstance().getArenaManager().getModes().get(Facility.getInstance().getFileManager().getArenas().getString("arenas." + id + ".mode"));
            }
        }
        locations = new HashMap();
        if(Facility.getInstance().getFileManager().getArenas().getConfiguration().contains("arenas." + id + ".locations")) {
            for(String location : Facility.getInstance().getFileManager().getArenas().getConfiguration().getConfigurationSection("arenas." + id + ".locations").getKeys(false)) {
                locations.put(location, Facility.getInstance().getFileManager().getArenas().getLocation("arenas." + id + ".locations." + location));
            }
        }
        if(Facility.getInstance().getFileManager().getArenas().getConfiguration().contains("arenas." + id + ".settings")) {
            settings = Facility.getInstance().getFileManager().getArenas().getStringListAsArrayList("arenas." + id + ".settings");
        } else {
            settings = new ArrayList<>();
        }
    }
    
    
    
}
