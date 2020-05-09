package me.velz.facility.utils;

import java.util.HashMap;
import me.velz.facility.Facility;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerSaveUtil {

    private final Facility plugin;

    public PlayerSaveUtil(Facility plugin) {
        this.plugin = plugin;
    }

    public HashMap<Player, ItemStack[]> inventorys = new HashMap();
    public HashMap<Player, ItemStack[]> armorslots = new HashMap();
    public HashMap<Player, Integer> levels = new HashMap();
    public HashMap<Player, Location> locations = new HashMap();

    public void reset(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setLevel(0);
        player.setExp(0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
    }
    
    public void save(Player player) {
        inventorys.put(player, player.getInventory().getContents());
        armorslots.put(player, player.getInventory().getArmorContents());
        levels.put(player, player.getLevel());
        locations.put(player, player.getLocation());
    }

    public void load(Player player) {
        player.getInventory().setContents(inventorys.get(player));
        player.getInventory().setArmorContents(armorslots.get(player));
        player.setLevel(levels.get(player));
        player.teleport(locations.get(player));
        inventorys.remove(player);
        armorslots.remove(player);
        levels.remove(player);
        locations.remove(player);
    }

}
