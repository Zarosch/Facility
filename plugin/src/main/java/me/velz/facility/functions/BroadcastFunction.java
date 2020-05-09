package me.velz.facility.functions;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityBroadcast;
import me.velz.facility.utils.FileBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastFunction implements Function {

    @Getter
    @Setter
    private boolean enabled = false;

    @Getter
    @Setter
    private Integer timer = 0, duration = 60;

    @Getter
    private final FileBuilder config = new FileBuilder(Facility.getInstance().getDataFolder() + "/functions", "broadcast.function.yml");

    @Getter
    private final HashMap<String, FacilityBroadcast> broadcasts = new HashMap();

    @Override
    public void onEnable() {
        this.onReload();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onSchedule() {
        if (!this.enabled) {
            if (this.timer >= this.duration) {
                boolean broadcasted = false;
                for (FacilityBroadcast broadcast : getBroadcasts().values()) {
                    if (broadcast.getBroadcasted() == false && broadcasted == false) {
                        broadcast.setBroadcasted(true);
                        broadcasted = true;
                        broadcast.getMessage().forEach((message) -> {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                        });
                        this.timer = 0;
                    }
                }
                if (broadcasted == false) {
                    getBroadcasts().values().forEach((broadcast) -> {
                        broadcast.setBroadcasted(false);
                    });
                }
            }
            this.timer++;
        }
    }

    @Override
    public void onReload() {
        this.getConfig().load();
        this.getConfig().addDefault("enabled", false);
        this.getConfig().addDefault("time", 120);
        if (!getConfig().getConfiguration().contains("broadcasts")) {
            this.getConfig().addDefault("broadcasts.default.message", new String[]{
                "&8[&3Info&8] &6Ändere diesen Broadcast in der Facility/functions/broadcasts.function.yml Datei.",
                "&8[&3Info&8] &eÄndere diesen Broadcast in der Facility/functions/broadcasts.function.yml Datei.",});
        }
        this.getConfig().save();
        this.setDuration(this.getConfig().getInt("time"));
        this.setEnabled(this.getConfig().getBoolean("enabled"));
        this.getBroadcasts().clear();
        for (String broadcast : this.getConfig().getConfiguration().getConfigurationSection("broadcasts").getKeys(false)) {
            this.getBroadcasts().put(broadcast, new FacilityBroadcast(broadcast, this.getConfig().getStringList("broadcasts." + broadcast + ".message")));
        }
    }

    @Override
    public void onAction(Player player, String action, String message) {
    }

}
