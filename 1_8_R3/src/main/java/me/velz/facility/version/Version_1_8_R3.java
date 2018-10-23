package me.velz.facility.version;

import java.lang.reflect.Field;
import java.util.Set;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Version_1_8_R3 implements Version {

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
        IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabHeader);
        try {
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public Sound getSound(String sound) {
        if (sound.equalsIgnoreCase("BLOCK_NOTE_BELL")) {
            return Sound.valueOf("NOTE_PLING");
        }
        if (sound.equalsIgnoreCase("BLOCK_NOTE_PLING")) {
            return Sound.valueOf("NOTE_PLING");
        }
        return null;
    }

    @Override
    public boolean isUnbreakable(ItemStack itemStack) {
        return itemStack.getItemMeta().spigot().isUnbreakable();
    }

    @Override
    public ItemStack setUnbreakable(ItemStack itemStack, boolean value) {
        itemStack.getItemMeta().spigot().setUnbreakable(value);
        return itemStack;
    }

    @Override
    public void hidePlayer(Player player, Player target) {
        player.hidePlayer(target);
    }

    @Override
    public void showPlayer(Player player, Player target) {
        player.showPlayer(target);
    }

    @Override
    public void sendComponentMessage(Player player, TextComponent component) {
        player.spigot().sendMessage(component);
    }

    @Override
    public String getSkullOwner(SkullMeta skullMeta) {
        return skullMeta.getOwner();
    }

    @Override
    public ItemStack getItemInMainHand(Player player) {
        return player.getItemInHand();
    }

    @Override
    public void setItemInMainHand(Player player, ItemStack itemStack) {
        player.setItemInHand(itemStack);
    }

    @Override
    public Integer getPing(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        return entityPlayer.ping;
    }

    @Override
    public Location getTargetBlock(Player player, Integer distance) {
        return player.getTargetBlock((Set<Material>) null, 32).getLocation();
    }

    @Override
    public Material getMaterial(String material) {
        if (material.equalsIgnoreCase("MOB_SPAWNER")) {
            return Material.MOB_SPAWNER;
        }
        if (material.equalsIgnoreCase("SKULL_ITEM")) {
            return Material.SKULL_ITEM;
        }
        if (material.equalsIgnoreCase("SKULL")) {
            return Material.SKULL;
        }
        if (material.equalsIgnoreCase("SIGN_POST")) {
            return Material.WALL_SIGN;
        }
        if (material.equalsIgnoreCase("STONE_SPADE")) {
            return Material.STONE_SPADE;
        }
        return null;
    }

}
