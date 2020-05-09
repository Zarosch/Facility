package me.velz.facility.commands;

import me.velz.facility.Facility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugmetaCommand implements CommandExecutor {

    private final Facility plugin;

    public DebugmetaCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        Player player = (Player)cs;
        for(String meta : plugin.getPlayers().get(player.getUniqueId().toString()).getMeta().keySet()) {
            player.sendMessage(meta + "=" + plugin.getPlayers().get(player.getUniqueId().toString()).getMeta().get(meta));
        }
        
        return true;
    }
    
}
