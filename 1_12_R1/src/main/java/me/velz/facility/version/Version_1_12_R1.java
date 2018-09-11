package me.velz.facility.version;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Version_1_12_R1 implements Version {

    public JavaPlugin plugin = null;

    @Override
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setTablist(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(new TextComponent(header), new TextComponent(footer));
    }

    @Override
    public Sound getSound(String sound) {
        return Sound.valueOf(sound);
    }

    @Override
    public boolean isUnbreakable(ItemStack itemStack) {
        return itemStack.getItemMeta().isUnbreakable();
    }

    @Override
    public ItemStack setUnbreakable(ItemStack itemStack, boolean value) {
        itemStack.getItemMeta().setUnbreakable(value);
        return itemStack;
    }

    @Override
    public void hidePlayer(Player player, Player target) {
        player.hidePlayer(this.plugin, target);
    }

    @Override
    public void showPlayer(Player player, Player target) {
        player.showPlayer(plugin, target);
    }

    @Override
    public void sendComponentMessage(Player player, TextComponent component) {
        player.sendMessage(component);
    }

    @Override
    public String getSkullOwner(SkullMeta skullMeta) {
        if (skullMeta.getOwningPlayer() == null) {
            return null;
        }
        return skullMeta.getOwningPlayer().getName();
    }

    @Override
    public ItemStack getItemInMainHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public void setItemInMainHand(Player player, ItemStack itemStack) {
        player.getInventory().setItemInMainHand(itemStack);
    }

    @Override
    public Integer getPing(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        return entityPlayer.ping;
    }

    @Override
    public Location getTargetBlock(Player player, Integer distance) {
        return player.getTargetBlock(null, distance).getLocation();
    }

}
