package me.velz.facility.objects;

import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FacilityArmorstand {
    
    @Getter
    public String world, name, permission;
    
    @Getter
    private final ArrayList<String> playerCommands;

    @Getter
    private final ArrayList<String> consoleCommands;

    public FacilityArmorstand(String world, String name, String permission, ArrayList<String> playerCommands, ArrayList<String> consoleCommands) {
        this.world = world;
        this.name = name;
        this.permission = permission;
        this.playerCommands = playerCommands;
        this.consoleCommands = consoleCommands;
    }
    
    public void use(Player player){
        playerCommands.forEach((command) -> {
            Bukkit.getServer().dispatchCommand(player, command.replaceAll("%player", player.getName()));
        });
        consoleCommands.forEach((command) -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player", player.getName()));
        });
    }
    
}
