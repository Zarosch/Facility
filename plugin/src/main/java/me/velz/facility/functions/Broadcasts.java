package me.velz.facility.functions;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.objects.FacilityBroadcast;
import me.velz.facility.utils.FileBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class Broadcasts {

    @Getter
    @Setter
    private Boolean enabled = false;

    @Getter
    @Setter
    private Integer timer = 0, duration = 60;

    @Getter
    private final FileBuilder config = new FileBuilder("/plugins/Facility/custom", "broadcasts.yml");

    @Getter
    private final HashMap<String, FacilityBroadcast> broadcasts = new HashMap();

    public void load() {
        this.getConfig().load();
        getBroadcasts().clear();
        setEnabled(this.getConfig().getBoolean("enabled"));
        setDuration(this.getConfig().getInt("timer"));
        if (getEnabled()) {
            for (String broadcast : this.getConfig().getConfiguration().getConfigurationSection("broadcasts").getKeys(false)) {
                final List<String> message = this.getConfig().getStringList("broadcasts." + broadcast + ".message");
                getBroadcasts().put(broadcast, new FacilityBroadcast(broadcast, message));
            }
        }
    }

    public void setdefaults() {
        this.getConfig().addDefault("enabled", true);
        this.getConfig().addDefault("time", 120);
        if (!getConfig().getConfiguration().contains("broadcasts")) {
            this.getConfig().addDefault("broadcasts.default.message", new String[]{
                "&fDefault Broadcast!"
            });
        }
        this.getConfig().save();
    }

    public void schedule() {
        if (enabled) {
            if (timer >= duration) {
                boolean broadcasted = false;
                for (FacilityBroadcast broadcast : getBroadcasts().values()) {
                    if (broadcast.getBroadcasted() == false && broadcasted == false) {
                        broadcast.setBroadcasted(true);
                        broadcasted = true;
                        for (String message : broadcast.getMessage()) {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }
                }
                if (broadcasted == false) {
                    for (FacilityBroadcast broadcast : getBroadcasts().values()) {
                        broadcast.setBroadcasted(false);
                    }
                } else {
                    timer = 0;
                }
            }
        }
    }

}
