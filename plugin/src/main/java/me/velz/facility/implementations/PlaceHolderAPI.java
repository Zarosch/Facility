package me.velz.facility.implementations;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import org.bukkit.entity.Player;

public class PlaceHolderAPI extends EZPlaceholderHook {

    private final Facility plugin;

    public PlaceHolderAPI(Facility plugin, String identifier) {
        super(plugin, "facility");
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player != null) {
            DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(player.getUniqueId().toString());
            if (identifier.equals("money")) {
                return String.valueOf(dbPlayer.getMoney());
            }
            if (identifier.equals("tokens")) {
                return String.valueOf(dbPlayer.getToken());
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
        }
        return null;
    }

}
