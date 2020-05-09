package me.velz.facility.functions;

import org.bukkit.entity.Player;

public interface Function {
    
    public void onEnable();
    
    public void onDisable();
    
    public void onReload();
    
    public void onAction(Player player, String action, String message);
    
    public void onSchedule();
    
}
