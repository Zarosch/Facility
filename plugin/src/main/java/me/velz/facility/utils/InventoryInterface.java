package me.velz.facility.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryInterface {
    
    public void getInventoryName();
    
    public void getInventorySize();
    
    public void clickedItem(ItemStack itemStack, Player player);
    
    public void closeInventory(Inventory inventory, Player player);
    
    public void openInventory(Inventory inventory, Player player);
    
}
