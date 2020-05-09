package me.velz.facility.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityInventory;
import me.velz.facility.objects.FacilityInventoryItem;
import me.velz.facility.utils.Actions;
import me.velz.facility.utils.FileBuilder;
import me.velz.facility.utils.ItemBuilder;
import me.velz.facility.utils.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryFunction implements Function, Listener, CommandExecutor {

    @Getter
    private FileBuilder config;

    @Getter
    private final HashMap<String, FacilityInventory> inventories = new HashMap();

    @Override
    public void onEnable() {
        config = new FileBuilder(Facility.getInstance().getDataFolder() + "/functions", "inventories.function.yml");
        Bukkit.getPluginManager().registerEvents(this, Facility.getInstance());
        Facility.getInstance().getCommand("inventory").setExecutor(this);
        this.onReload();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {
        this.getConfig().load();

        if (!config.getConfiguration().contains("inventories")) {
            config.addDefault("inventories.test.displayName", "&6Inventory");
            config.addDefault("inventories.test.permission", "facility.inventory.test");
            config.addDefault("inventories.test.size", 18);
            config.addDefault("inventories.test.items.item.item", new ItemBuilder().setDisplayName("§6Test").setMaterial(Material.STONE).build());
            config.addDefault("inventories.test.items.item.slot", 1);
            config.addDefault("inventories.test.items.item.permission", "facility.inventory.test");
            config.addDefault("inventories.test.items.item.actions.right", new String[]{
                "message>&eRight click"
            });
            config.addDefault("inventories.test.items.item.actions.left", new String[]{
                "message>&eLeft click"
            });
            config.addDefault("inventories.test.items.item.actions.leftshift", new String[]{
                "message>&eShift + Left click"
            });
            config.addDefault("inventories.test.items.item.actions.rightshift", new String[]{
                "message>&eShift + Right click"
            });
            config.save();
        }

        if (!this.getConfig().getConfiguration().contains("templates")) {
            // inventory setTemplate test 3 shop buy=250;sell=15
            this.getConfig().addDefault("templates.default.items.item.lore", new String[]{
                "",
                "&6Rechtsklick zum Kaufen",
                "&6Kaufpreis: &f%buy",
                "",
                "&6Linksklick zum Verkaufen",
                "&6Verkaufspreis: &f%sell",
                ""
            });
            this.getConfig().addDefault("templates.default.items.permission", "facility.inventory.shop");
            config.addDefault("templates.default.items.actions.right", new String[]{
                "takemoney>%buy"
            });
            config.addDefault("templates.default.items.actions.left", new String[]{
                "givemoney>%sell"
            });
            config.addDefault("templates.default.items.actions.leftshift", new String[]{
                "message>&eShift + Left click"
            });
            config.addDefault("templates.default.items.actions.rightshift", new String[]{
                "message>&eShift + Right click"
            });
            config.save();
        }

        for (String invid : config.getConfiguration().getConfigurationSection("inventories").getKeys(false)) {
            String displayname = ChatColor.translateAlternateColorCodes('&', config.getString("inventories." + invid + ".displayName"));
            String permission = config.getString("inventories." + invid + ".permission");
            Integer size = config.getInt("inventories." + invid + ".size");
            HashMap<Integer, FacilityInventoryItem> items = new HashMap();
            for (String itemid : config.getConfiguration().getConfigurationSection("inventories." + invid + ".items").getKeys(false)) {
                Integer itemslot = config.getInt("inventories." + invid + ".items." + itemid + ".slot");
                String itempermission = config.getString("inventories." + invid + ".items." + itemid + ".permission");
                HashMap<String, ArrayList<String>> itemactions = new HashMap();
                itemactions.put("left", new ArrayList<>());
                itemactions.put("leftshift", new ArrayList<>());
                itemactions.put("right", new ArrayList<>());
                itemactions.put("rightshift", new ArrayList<>());
                if (config.getConfiguration().contains("inventories." + invid + ".items." + itemid + ".actions.left")) {
                    itemactions.put("left", config.getStringListAsArrayList("inventories." + invid + ".items." + itemid + ".actions.left"));
                }
                if (config.getConfiguration().contains("inventories." + invid + ".items." + itemid + ".actions.leftshift")) {
                    itemactions.put("leftshift", config.getStringListAsArrayList("inventories." + invid + ".items." + itemid + ".actions.leftshift"));
                }
                if (config.getConfiguration().contains("inventories." + invid + ".items." + itemid + ".actions.right")) {
                    itemactions.put("right", config.getStringListAsArrayList("inventories." + invid + ".items." + itemid + ".actions.right"));
                }
                if (config.getConfiguration().contains("inventories." + invid + ".items." + itemid + ".actions.rightshift")) {
                    itemactions.put("rightshift", config.getStringListAsArrayList("inventories." + invid + ".items." + itemid + ".actions.rightshift"));
                }
                if (config.getConfiguration().contains("inventories." + invid + ".items." + itemid + ".actions.shows")) {
                    itemactions.put("shows", config.getStringListAsArrayList("inventories." + invid + ".items." + itemid + ".actions.shows"));
                }
                if (config.getConfiguration().contains("inventories." + invid + ".items." + itemid + ".actions.click")) {
                    itemactions.put("click", config.getStringListAsArrayList("inventories." + invid + ".items." + itemid + ".actions.click"));
                }
                ItemStack stack = config.getItemStack("inventories." + invid + ".items." + itemid + ".item");
                FacilityInventoryItem inventoryItem = new FacilityInventoryItem(itemid, stack, itemslot, itempermission, itemactions);
                items.put(itemslot, inventoryItem);
            }
            FacilityInventory inventory = new FacilityInventory(displayname, invid, permission, size, items);
            inventories.put(invid, inventory);
        }

        this.getConfig().save();
    }

    public void openInventory(Player player, String invid) {
        if (inventories.containsKey(invid)) {
            FacilityInventory fInventory = inventories.get(invid);
            Inventory inventory = Bukkit.createInventory(null, fInventory.getSize(), fInventory.getDisplayName());
            for (Integer slot : fInventory.getItems().keySet()) {
                boolean show = true;
                for (String action : fInventory.getItems().get(slot).getActions().get("shows")) {
                    if (Actions.cancelAction(player, Actions.run(player, action))) {
                        show = false;
                    }
                }
                if (show) {
                    inventory.setItem(slot, fInventory.getItems().get(slot).getItem());
                }
            }
            player.openInventory(inventory);
        }
    }

    @Override
    public void onSchedule() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView() == null) {
            return;
        }
        if (event.getView().getTitle() == null) {
            return;
        }
        for (String inventory : inventories.keySet()) {
            if (inventories.get(inventory).getDisplayName().equalsIgnoreCase(event.getView().getTitle())) {
                Player player = (Player) event.getWhoClicked();
                event.setCancelled(true);
                if (!player.hasPermission(inventories.get(inventory).getPermission())) {
                    player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                    return;
                }
                for (FacilityInventoryItem item : inventories.get(inventory).getItems().values()) {
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    if (event.getCurrentItem().getItemMeta() == null) {
                        return;
                    }
                    if (event.getCurrentItem().getItemMeta().getDisplayName() == null) {
                        return;
                    }
                    if (item.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(event.getCurrentItem().getItemMeta().getDisplayName())) {
                        if (event.getClick() == ClickType.LEFT && item.getActions().containsKey("left")) {
                            for (String action : item.getActions().get("left")) {
                                if (Actions.cancelAction(player, Actions.run(player, action))) {
                                    return;
                                }
                            }
                        }
                        if (event.getClick() == ClickType.SHIFT_LEFT && item.getActions().containsKey("leftshift")) {
                            for (String action : item.getActions().get("leftshift")) {
                                if (Actions.cancelAction(player, Actions.run(player, action))) {
                                    return;
                                }
                            }
                        }
                        if (event.getClick() == ClickType.RIGHT && item.getActions().containsKey("right")) {
                            for (String action : item.getActions().get("right")) {
                                if (Actions.cancelAction(player, Actions.run(player, action))) {
                                    return;
                                }
                            }
                        }
                        if (event.getClick() == ClickType.SHIFT_RIGHT && item.getActions().containsKey("rightshift")) {
                            for (String action : item.getActions().get("rightshift")) {
                                if (Actions.cancelAction(player, Actions.run(player, action))) {
                                    return;
                                }
                            }
                        }
                        if (item.getActions().containsKey("click")) {
                            for (String action : item.getActions().get("click")) {
                                if (Actions.cancelAction(player, Actions.run(player, action))) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (event.getView().getTitle().startsWith("§8Inventory Move #")) {
            if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.HOTBAR_SWAP) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.DROP_ALL_SLOT) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.DROP_ALL_CURSOR) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.DROP_ONE_CURSOR) {
                event.setCancelled(true);
            }
            if (event.getAction() == InventoryAction.DROP_ONE_SLOT) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().startsWith("§8Inventory Add #")) {
            String inv = event.getView().getTitle().split("#")[1];
            int nameI = 0;
            for (ItemStack stack : event.getInventory().getContents()) {
                if (stack != null) {
                    nameI++;
                    Integer slot = 0;
                    boolean slotFound = false;
                    for (int i = 0; i != inventories.get(inv).getSize(); i++) {
                        if (!inventories.get(inv).getItems().containsKey(i)) {
                            slot = i;
                            slotFound = true;
                        }
                    }
                    if (!slotFound) {
                        event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_SLOTNOTFOUND.getLocal());
                    } else {
                        String name = System.currentTimeMillis() + "-" + nameI;
                        config.set("inventories." + inv + ".items." + name + ".item", stack);
                        config.set("inventories." + inv + ".items." + name + ".slot", slot);
                        config.set("inventories." + inv + ".items." + name + ".permission", "facility.inventory." + inv);
                        config.set("inventories." + inv + ".items." + name + ".actions.right", new String[]{
                            "message>&eRight click"
                        });
                        config.set("inventories." + inv + ".items." + name + ".actions.left", new String[]{
                            "message>&eLeft click"
                        });
                        config.set("inventories." + inv + ".items." + name + ".actions.leftshift", new String[]{
                            "message>&eShift + Left click"
                        });
                        config.set("inventories." + inv + ".items." + name + ".actions.rightshift", new String[]{
                            "message>&eShift + Right click"
                        });
                        config.save();
                        this.onReload();
                    }
                }
            }
            event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_ITEMSADDED.getLocal());
        }
        if (event.getView().getTitle().startsWith("§8Inventory Move #")) {
            String inv = event.getView().getTitle().split("#")[1];
            FacilityInventory facilityInventory = inventories.get(inv);
            HashMap<Integer, FacilityInventoryItem> newItemsMap = new HashMap();
            int i = 0;
            for (ItemStack stack : event.getInventory().getContents()) {
                if (stack != null) {
                    Integer oldSlot = Integer.valueOf(stack.getItemMeta().getLore().get(stack.getItemMeta().getLore().size() - 1).replaceAll("#", ""));
                    String itemId = facilityInventory.getItems().get(oldSlot).getId();
                    this.getConfig().set("inventories." + inv + ".items." + itemId + ".slot", i);
                }
                i++;
            }
            this.getConfig().save();
            this.onReload();
            event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_ITEMSMOVED.getLocal());
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("inventory")) {

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("open")) {
                    if (!cs.hasPermission("facility.functions.inventory.open")) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return true;
                    }
                    if (args.length < 2) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/inventory open <Inventory> [Player]"));
                        return true;
                    }
                    Player player = null;
                    if (args.length >= 3) {
                        if (!cs.hasPermission("facility.functions.inventory.open.other")) {
                            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                            return true;
                        }
                        if (Bukkit.getPlayer(args[2]) == null) {
                            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                            return true;
                        }
                        player = Bukkit.getPlayer(args[2]);
                    } else {
                        player = (Player) cs;
                    }
                    String invId = args[1];
                    if (!inventories.containsKey(invId)) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_NOTFOUND.getLocal());
                        return true;
                    }
                    this.openInventory(player, invId);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_OPEN.getLocal());
                    return true;
                }

                if (args[0].equalsIgnoreCase("addinv")) {
                    if (!cs.hasPermission("facility.functions.inventory.addinv")) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return true;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("moveitems")) {
                    if (!cs.hasPermission("facility.functions.inventory.moveitems")) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return true;
                    }
                    if (args.length != 2) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/inventory moveitems <Inventory>"));
                        return true;
                    }
                    String invId = args[1];
                    if (!inventories.containsKey(invId)) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_NOTFOUND.getLocal());
                        return true;
                    }
                    FacilityInventory facilityInventory = inventories.get(invId);
                    Player player = (Player) cs;
                    Inventory inventory = Bukkit.createInventory(null, facilityInventory.getSize(), "§8Inventory Move #" + invId);
                    for (Integer slot : facilityInventory.getItems().keySet()) {
                        ItemStack is = facilityInventory.getItems().get(slot).getItem().clone();
                        ItemMeta im = is.getItemMeta();
                        if (im.hasLore()) {
                            List<String> lore = is.getItemMeta().getLore();
                            lore.add("#" + slot);
                            im.setLore(lore);
                        } else {
                            List<String> lore = new ArrayList<>();
                            lore.add("#" + slot);
                            im.setLore(lore);
                        }
                        is.setItemMeta(im);
                        inventory.setItem(slot, is);
                    }
                    player.openInventory(inventory);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_ITEMSMOVE.getLocal());
                    return true;
                }

                if (args[0].equalsIgnoreCase("additems")) {
                    if (!cs.hasPermission("facility.functions.inventory.additems")) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return true;
                    }
                    if (args.length != 2) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/inventory additems <Inventory>"));
                        return true;
                    }
                    String invId = args[1];
                    if (!inventories.containsKey(invId)) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_NOTFOUND.getLocal());
                        return true;
                    }
                    Player player = (Player) cs;
                    Inventory inventory = Bukkit.createInventory(null, 18, "§8Inventory Add #" + invId);
                    player.openInventory(inventory);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_ITEMSADD.getLocal());
                    return true;
                }

                if (args[0].equalsIgnoreCase("list")) {
                    if (!cs.hasPermission("facility.functions.inventory.list")) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return true;
                    }

                    if (inventories.isEmpty()) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_NOINVENTORIES.getLocal());
                        return true;
                    }

                    String invs = "";
                    for (String inv : inventories.keySet()) {
                        invs = invs + inv + ", ";
                    }
                    invs = invs.substring(0, invs.length() - 2);
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.FUNC_INVENTORY_LIST.getLocal().replaceAll("%invs", invs));
                    return true;
                }
            }

            if (!cs.hasPermission("facility.functions.inventory")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }

            cs.sendMessage("");
            cs.sendMessage(MessageUtil.FUNC_INVENTORY_HELP_OPEN.getLocal());
            cs.sendMessage(MessageUtil.FUNC_INVENTORY_HELP_ADD.getLocal());
            cs.sendMessage(MessageUtil.FUNC_INVENTORY_HELP_MOVE.getLocal());
            cs.sendMessage(MessageUtil.FUNC_INVENTORY_HELP_LIST.getLocal());
            cs.sendMessage("");

        }

        return true;
    }

    @Override
    public void onAction(Player player, String action, String message) {
        if (action.equalsIgnoreCase("openinventory")) {
            openInventory(player, message);
        }
    }

}
