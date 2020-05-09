package me.velz.facility.version;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public interface Version {
        
    public JavaPlugin getPlugin();
    
    public void setPlugin(JavaPlugin plugin);
    
    public void setTablist(Player player, String header, String footer);
    
    public Sound getSound(String sound);
    
    public boolean isUnbreakable(ItemStack itemStack);
    
    public ItemStack setUnbreakable(ItemStack itemStack, boolean value);
    
    public void hidePlayer(Player player, Player target);
    
    public void showPlayer(Player player, Player target);
    
    public void sendComponentMessage(Player player, TextComponent component);
    
    public String getSkullOwner(SkullMeta skullMeta);
    
    public ItemStack getItemInMainHand(Player player);
    
    public void setItemInMainHand(Player player, ItemStack itemStack);
    
    public Integer getPing(Player player);
    
    public Location getTargetBlock(Player player, Integer distance);
    
    public Material getMaterial(String material);
    
    public void addPlayerToBoat(Player player);
    
    public boolean isSign(Material material);
    
}
