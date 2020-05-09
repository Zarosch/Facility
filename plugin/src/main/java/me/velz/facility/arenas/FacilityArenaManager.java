package me.velz.facility.arenas;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.utils.PlayerSaveUtil;
import org.bukkit.Location;

public class FacilityArenaManager {
    
    private final Facility plugin;

    public FacilityArenaManager(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Getter
    private HashMap<String, FacilityArenaMode> modes;
    
    @Getter
    private HashMap<String, FacilityArena> arenas;
    
    @Getter
    private PlayerSaveUtil playerSaveUtil;
    
    public void load() {
        if(playerSaveUtil == null) {
            playerSaveUtil = new PlayerSaveUtil(plugin);
        }
        arenas = new HashMap();
        modes = new HashMap();
        if(plugin.getFileManager().getArenas().getConfiguration().contains("modes")) {
            for(String mode : plugin.getFileManager().getArenas().getConfiguration().getConfigurationSection("modes").getKeys(false)) {
                FacilityArenaMode facilityMode = new FacilityArenaMode(mode);
                modes.put(mode, facilityMode);
            }
        }
        if(plugin.getFileManager().getArenas().getConfiguration().contains("arenas")) {
            for(String arena : plugin.getFileManager().getArenas().getConfiguration().getConfigurationSection("arenas").getKeys(false)) {
                FacilityArena facilityArena = new FacilityArena(arena);
                arenas.put(arena, facilityArena);
            }
        }
    }
    
    public void addArena(String id) {
        plugin.getFileManager().getArenas().set("arenas." + id + ".name", id);
        plugin.getFileManager().getArenas().save();
        arenas.put(id, new FacilityArena(id));
    }
    
    public void removeArena(String id) {
        plugin.getFileManager().getArenas().getConfiguration().set("arenas." + id, null);
        plugin.getFileManager().getArenas().save();
        arenas.remove(id);
    }
    
    public void addLocation(String id, String locname, Location loc) {
        plugin.getFileManager().getArenas().set("arenas." + id + ".locations." + locname, loc);
        plugin.getFileManager().getArenas().save();
        arenas.get(id).getLocations().put(locname, loc);
    }
    
    public void removeLocation(String id, String locname) {
        plugin.getFileManager().getArenas().getConfiguration().set("arenas." + id + ".locations." + locname, null);
        plugin.getFileManager().getArenas().save();
        arenas.get(id).getLocations().remove(locname);
    }
    
    public String getLocationList(FacilityArena arena) {
        String str = "";
        if(arena.getLocations().isEmpty()) {
            return "Keine Locations gesetzt";
        }
        for(String loc : arena.getLocations().keySet()) {
            str = str + loc + ", ";
        }
        str = str.substring(0, str.length()-2);
        return str;
    }
    
    public String getArenaList() {
        String str = "";
        if(this.getArenas().isEmpty()) {
            return "Keine Arenen gesetzt";
        }
        for(String arena : this.getArenas().keySet()) {
            str = str + arena + ", ";
        }
        str = str.substring(0, str.length()-2);
        return str;
    }
    
    public String getModeList() {
        String str = "";
        if(this.getModes().isEmpty()) {
            return "Keine Spielmodus gesetzt";
        }
        for(String mode : this.getModes().keySet()) {
            str = str + mode + ", ";
        }
        str = str.substring(0, str.length()-2);
        return str;
    }
    
    public void addMode(String id) {
        plugin.getFileManager().getArenas().set("modes." + id + ".settings", new ArrayList<>());
        plugin.getFileManager().getArenas().save();
        modes.put(id, new FacilityArenaMode(id));
    }
    
    public void removeMode(String id) {
        plugin.getFileManager().getArenas().getConfiguration().set("modes." + id, null);
        plugin.getFileManager().getArenas().save();
        modes.remove(id);
    }
    
    public void setMode(String id, String mode) {
        FacilityArena arena = this.arenas.get(id);
        FacilityArenaMode arenaMode = this.modes.get(mode);
        arena.setMode(arenaMode);
    }
    
    public void addSetting(String id, String target, String setting) {
        if(target.equalsIgnoreCase("arena")) {
            FacilityArena arena = this.arenas.get(id);
            arena.getSettings().add(setting);
            plugin.getFileManager().getArenas().set("arenas." + id + ".settings", arena.getSettings());
            plugin.getFileManager().getArenas().save();
        }
        if(target.equalsIgnoreCase("mode")) {
            FacilityArenaMode mode = this.modes.get(id);
            mode.getSettings().add(setting);
            plugin.getFileManager().getArenas().set("modes." + id + ".settings", mode.getSettings());
            plugin.getFileManager().getArenas().save();
        }
    }
    
    public void removeSetting(String id, String target, String setting) {
        if(target.equalsIgnoreCase("arena")) {
            FacilityArena arena = this.arenas.get(id);
            arena.getSettings().remove(setting);
            plugin.getFileManager().getArenas().set("arenas." + id + ".settings", arena.getSettings());
            plugin.getFileManager().getArenas().save();
        }
        if(target.equalsIgnoreCase("mode")) {
            FacilityArenaMode mode = this.modes.get(id);
            mode.getSettings().remove(setting);
            plugin.getFileManager().getArenas().set("modes." + id + ".settings", mode.getSettings());
            plugin.getFileManager().getArenas().save();
        }
    }
    
    public String listSettings(String id) {
        FacilityArena arena = this.arenas.get(id);
        String str = "";
        if(arena.getSettings().isEmpty()) {
            return "Keine Settings gesetzt";
        }
        for(String setting : arena.getSettings()) {
            str = str + setting + ", ";
        }
        str = str.substring(0, str.length()-2);
        return str;
    }
    
}
