package me.velz.facility.objects;

import java.util.ArrayList;
import lombok.Getter;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FacilityKit {
    
    @Getter
    private final String name, cooldown, permission;
    
    @Getter
    private final ArrayList<ItemStack> items;

    public FacilityKit(String name, String cooldown, String permission, ArrayList<ItemStack> items) {
        this.name = name;
        this.cooldown = cooldown;
        this.permission = permission;
        this.items = items;
    }
    
    public void give(Player player) {
        items.forEach((item) -> {
            player.getInventory().addItem(item);
        });
        player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_GIVEKIT.getLocal().replaceAll("%kit", name));
    }
    
}
