package me.velz.facility.functions;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityCommand;
import me.velz.facility.utils.Actions;
import me.velz.facility.utils.FileBuilder;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandFunction implements Function, Listener {

    @Getter
    private FileBuilder config;

    @Getter
    private final HashMap<String, FacilityCommand> commands = new HashMap();

    @Override
    public void onEnable() {
        config = new FileBuilder(Facility.getInstance().getDataFolder() + "/functions", "command.function.yml");
        Bukkit.getPluginManager().registerEvents(this, Facility.getInstance());
        this.onReload();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onReload() {
        this.getConfig().load();

        if (!config.getConfiguration().contains("commands")) {
            config.addDefault("commands.test.command", "/test");
            config.addDefault("commands.test.permission", "facility.command.test");
            config.addDefault("commands.test.actions", new String[]{
                "message>&eDas ist eine Test Nachricht.",
                "command>spawn"
            });
        }

        this.getConfig().save();
        this.getCommands().clear();
        for (String id : config.getConfiguration().getConfigurationSection("commands").getKeys(false)) {
            String command = config.getString("commands." + id + ".command");
            String permission = config.getString("commands." + id + ".permission");
            ArrayList<String> actions = config.getStringListAsArrayList("commands." + id + ".actions");
            this.getCommands().put(id, new FacilityCommand(id, command, permission, actions));
        }
    }

    @Override
    public void onSchedule() {
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        for (FacilityCommand command : getCommands().values()) {
            if (command.getCommand().equalsIgnoreCase(event.getMessage())) {
                event.setCancelled(true);
                if (!event.getPlayer().hasPermission(command.getPermission())) {
                    event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                    return;
                }
                for (String action : command.getActions()) {
                    if (Actions.cancelAction(event.getPlayer(), Actions.run(event.getPlayer(), action))) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onAction(Player player, String action, String message) {
    }

}
