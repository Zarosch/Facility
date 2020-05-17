package me.velz.facility.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import me.velz.facility.FacilityAPI;
import me.velz.facility.functions.Function;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Actions {

    public static String run(Player player, String string) {
        String[] act = string.split(">");
        String action = act[0];
        String message = act[1];
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = Facility.getInstance().getImplementations().getPlaceholderapi().replace(player, message);
        }
        String customerror = null;
        if (action.equalsIgnoreCase("customerror")) {
            customerror = message;
        }
        if (action.equalsIgnoreCase("removecustomerror")) {
            customerror = null;
        }
        message = message.replaceAll("%player_name%", player.getName());
        if (action.equalsIgnoreCase("takemoney")) {
            if (!FacilityAPI.hasMoney(player, Double.valueOf(message))) {
                if (customerror != null) {
                    return "custom;" + customerror;
                }
                return "not_enough_money";
            }
        }
        if (action.equalsIgnoreCase("taketokens")) {
            if (!FacilityAPI.hasMoney(player, Double.valueOf(message))) {
                if (customerror != null) {
                    return "custom;" + customerror;
                }
                return "not_enough_tokens";
            }
        }
        if (action.equalsIgnoreCase("takeitem")) {
            // material;amount;displayname
            String[] item = message.split(";");
            ItemStack stack = null;
            if(item.length == 1) {
                stack = new ItemBuilder().setMaterial(Material.getMaterial(item[0])).setAmount(1).build();
            }
            if (item.length == 2) {
                stack = new ItemBuilder().setMaterial(Material.getMaterial(item[0])).setAmount(Integer.valueOf(item[1])).build();
            }
            if(item.length == 3) {
                stack = new ItemBuilder().setDisplayName(ChatColor.translateAlternateColorCodes('&', item[2])).setMaterial(Material.getMaterial(item[0])).setAmount(Integer.valueOf(item[1])).build();
            }
            if (stack != null) {
                if (!inventoryContains(player.getInventory(), stack)) {
                    if (customerror != null) {
                        return "custom;" + customerror;
                    }
                    return "not_enough_items";
                }
                removeFromInventory(player.getInventory(), stack);
            }
        }
        if(action.equalsIgnoreCase("giveitem")) {
            String[] item = message.split(";");
            ItemStack stack = null;
            if(item.length == 1) {
                stack = new ItemBuilder().setMaterial(Material.getMaterial(item[0])).setAmount(1).build();
            }
            if(item.length == 2) {
                stack = new ItemBuilder().setMaterial(Material.getMaterial(item[0])).setAmount(Integer.valueOf(item[1])).build();
            }
            if(item.length == 3) {
                stack = new ItemBuilder().setDisplayName(ChatColor.translateAlternateColorCodes('&', item[2])).setMaterial(Material.getMaterial(item[0])).setAmount(Integer.valueOf(item[1])).build();
            }
            if (stack != null) {
                player.getInventory().addItem(stack);
            }
        }
        if (action.equalsIgnoreCase("message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        if (action.equalsIgnoreCase("console")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message);
        }
        
        if (action.equalsIgnoreCase("date")) {
            Date date1;
            try {
                // Example: 31-12-2019
                date1 = new SimpleDateFormat("dd-MM-yyyy").parse(message);
                if (!date1.after(new Date())) {
                    if (customerror != null) {
                        return "custom;" + customerror;
                    }
                    return "unvalid_date";
                }
            } catch (ParseException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (action.equalsIgnoreCase("errorifsamedate")) {
            String[] timeframe = message.split(";");
            try {
                // Example: 31-12-2019
                Date date1 = null;
                if (timeframe[0].equalsIgnoreCase("now")) {
                    date1 = new Date();
                } else {
                    date1 = new SimpleDateFormat("dd-MM-yyyy").parse(timeframe[0]);
                }
                Date date2 = null;
                if (timeframe[1].equalsIgnoreCase("now")) {
                    date2 = new Date();
                } else {
                    date2 = new SimpleDateFormat("dd-MM-yyyy").parse(timeframe[1]);
                }
                if (date1.equals(date2)) {
                    if (customerror != null) {
                        return "custom;" + customerror;
                    }
                    return "unvalid_timeframe";
                }
            } catch (ParseException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (action.equalsIgnoreCase("timeframe")) {
            String[] timeframe = message.split(";");
            try {
                // Example: 31-12-2019
                Date date1 = null;
                if (timeframe[0].equalsIgnoreCase("now")) {
                    date1 = new Date();
                } else {
                    date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeframe[0]);
                }
                Date date2 = null;
                if (timeframe[1].equalsIgnoreCase("now")) {
                    date2 = new Date();
                } else {
                    date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(timeframe[1]);
                }
                if (!new Date().after(date1) || !new Date().before(date2)) {
                    if (customerror != null) {
                        return "custom;" + customerror;
                    }
                    return "unvalid_timeframe";
                }
            } catch (ParseException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (action.equalsIgnoreCase("permission")) {
            if (!player.hasPermission(message)) {
                if (customerror != null) {
                    return "custom;" + customerror;
                }
                return "no_permission";
            }
        }
        if (action.equalsIgnoreCase("!permission")) {
            if (player.hasPermission(message)) {
                if (customerror != null) {
                    return "custom;" + customerror;
                }
                return "no_permission";
            }
        }
        if (action.equalsIgnoreCase("command")) {
            Bukkit.dispatchCommand(player, message);
        }
        if (action.equalsIgnoreCase("broadcast")) {
            Bukkit.broadcastMessage(message);
        }
        if (action.equalsIgnoreCase("setmeta")) {
            String key = message.split("=")[0];
            String value = message.split("=")[1];
            Facility.getInstance().getDatabase().addMetadata(player.getUniqueId().toString(), key, value);
            if (Facility.getInstance().getPlayers().containsKey(player.getUniqueId().toString())) {
                Facility.getInstance().getPlayers().get(player.getUniqueId().toString()).getMeta().put(key, value);
            }
        }
        if (action.equalsIgnoreCase("setmetaifnotset")) {
            String key = message.split("=")[0];
            String value = message.split("=")[1];
            if (Facility.getInstance().getPlayers().containsKey(player.getUniqueId().toString())) {
                if (!Facility.getInstance().getPlayers().get(player.getUniqueId().toString()).getMeta().containsKey(key)) {
                    Facility.getInstance().getDatabase().addMetadata(player.getUniqueId().toString(), key, value);
                    Facility.getInstance().getPlayers().get(player.getUniqueId().toString()).getMeta().put(key, value);
                }
            }
        }
        if (action.equalsIgnoreCase("removemeta")) {
            Facility.getInstance().getDatabase().removeMetadata(player.getUniqueId().toString(), message);
            if (Facility.getInstance().getPlayers().containsKey(player.getUniqueId().toString())) {
                if (Facility.getInstance().getPlayers().get(player.getUniqueId().toString()).getMeta().containsKey(message)) {
                    Facility.getInstance().getPlayers().get(player.getUniqueId().toString()).getMeta().remove(message);
                }
            }
        }
        for (Function function : Facility.getInstance().getFunctionManager().functions) {
            function.onAction(player, action, message);
        }
        return null;
    }

    public static boolean cancelAction(Player player, String result) {
        if (result != null) {
            if (result.startsWith("custom;")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', result.split(";")[1]));
                return true;
            }
            if (result.equalsIgnoreCase("not_enough_money")) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MONEY_NOTENOUGH.getLocal());
                return true;
            }
            if (result.equalsIgnoreCase("not_enough_tokens")) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_NOTENOUGH.getLocal());
                return true;
            }
            if (result.equalsIgnoreCase("not_enough_items")) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOTENOUGHITEMS.getLocal());
                return true;
            }
            if (result.equalsIgnoreCase("unvalid_date")) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_DATE.getLocal());
                return true;
            }
            if (result.equalsIgnoreCase("unvalid_timeframe")) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_DATE.getLocal());
                return true;
            }
            if (result.equalsIgnoreCase("no_permission")) {
                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
        }
        return false;
    }

    private static boolean inventoryContains(Inventory inventory, ItemStack item) {
        int count = 0;
        ItemStack[] items = inventory.getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() == item.getType() && items[i].getDurability() == item.getDurability()) {
                count += items[i].getAmount();
            }
            if (count >= item.getAmount()) {
                return true;
            }
        }
        return false;
    }

    private static void removeFromInventory(Inventory inventory, ItemStack item) {
        int amt = item.getAmount();
        ItemStack[] items = inventory.getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() == item.getType() && items[i].getDurability() == item.getDurability()) {
                if (items[i].getAmount() > amt) {
                    items[i].setAmount(items[i].getAmount() - amt);
                    break;
                } else if (items[i].getAmount() == amt) {
                    items[i] = null;
                    break;
                } else {
                    amt -= items[i].getAmount();
                    items[i] = null;
                }
            }
        }
        inventory.setContents(items);
    }

}
