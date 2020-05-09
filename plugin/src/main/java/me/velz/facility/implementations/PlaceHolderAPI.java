package me.velz.facility.implementations;

import java.text.SimpleDateFormat;
import java.util.Date;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import org.bukkit.entity.Player;

public class PlaceHolderAPI extends PlaceholderExpansion {

    private final Facility plugin;

    public PlaceHolderAPI(Facility plugin, String identifier) {
        this.plugin = plugin;
    }
    
    public String replace(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player != null) {
            DatabasePlayer dbPlayer = plugin.getDatabase().getUser(player.getUniqueId().toString());
            if (identifier.equals("money")) {
                return String.valueOf((int)dbPlayer.getMoney());
            }
            if (identifier.equals("tokens")) {
                return String.valueOf((int)dbPlayer.getToken());
            }
            if (identifier.equals("playtime")) {
                return plugin.getTools().getPlaytime(dbPlayer.getPlaytime());
            }
            if (identifier.equals("playtimeshort")) {
                return plugin.getTools().getPlaytimeShort(dbPlayer.getPlaytime());
            }
            if (identifier.equals("playtimedigitals")) {
                return plugin.getTools().getPlaytimeDigis(dbPlayer.getPlaytime());
            }
            if(identifier.startsWith("meta_")) {
                return dbPlayer.getMeta().get(identifier.split("_")[1]);
            }
            if(identifier.equals("date_now")) {
                return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            }
        }
        return null;
    }

    @Override
    public String getIdentifier() {
        return "facility";
    }

    @Override
    public String getPlugin() {
        return "Facility";
    }

    @Override
    public String getAuthor() {
        return "Zarosch";
    }

    @Override
    public String getVersion() {
        return "Version 1.0";
    }

}
