package me.velz.facility.version;

import java.lang.reflect.Field;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Location;
import org.bukkit.Material;
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
        IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field fieldA = packet.getClass().getDeclaredField("b");
            fieldA.setAccessible(true);
            fieldA.set(packet, tabHeader);
            Field fieldB = packet.getClass().getDeclaredField("b");
            fieldB.setAccessible(true);
            fieldB.set(packet, tabFooter);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            System.out.println("[Facility] Error while set PacketPlayOutPlayerListHeaderFooter [Version: 1_12_R1]");
        } finally {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
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
        player.spigot().sendMessage(component);
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

    @Override
    public Material getMaterial(String material) {
        if(material.equalsIgnoreCase("MOB_SPAWNER")) {
            return Material.MOB_SPAWNER;
        }
        if(material.equalsIgnoreCase("SKULL_ITEM")) {
            return Material.SKULL_ITEM;
        }
        if(material.equalsIgnoreCase("SKULL")) {
            return Material.SKULL;
        }
        if(material.equalsIgnoreCase("SIGN_POST")) {
            return Material.SIGN_POST;
        }
        if(material.equalsIgnoreCase("STONE_SPADE")) {
            return Material.STONE_SPADE;
        }
        return null;
    }

}
