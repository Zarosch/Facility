package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.utils.ItemBuilder;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WarpListCommand implements CommandExecutor, Listener {

    private final Facility plugin;

    public WarpListCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.warplist")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        Integer page = 1;

        if (args.length >= 1) {
            try {
                if (plugin.getWarps().size() > (Integer.valueOf(args[0])-1) * 27) {
                    page = Integer.valueOf(args[0]);
                } else {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.WARPLIST_PAGENOTFOUND.getLocal());
                    return true;
                }
            } catch (NumberFormatException exception) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
        }

        Inventory inventory = Bukkit.createInventory(null, 36, "§8Warps [Page " + page + "]");

        int itemint = 0;
        int amount = 0;
        for (String warp : plugin.getWarps().keySet()) {
            itemint++;
            if (itemint >= ((page - 1) * 27) && amount != 27) {
                ItemStack item = new ItemBuilder().setMaterial(plugin.getVersion().getMaterial("SIGN")).setDisplayName("§6" + warp).build();
                inventory.addItem(item);
                amount++;
            }
        }
        if (page != 1) {
            ItemStack pageback = new ItemBuilder().setMaterial(Material.BOOK).setDisplayName("§6Zurück").build();
            inventory.setItem(27, pageback);
        }
        if(plugin.getWarps().size() > page * 27) {
            ItemStack pagenext = new ItemBuilder().setMaterial(Material.BOOK).setDisplayName("§6Weiter").build();
            inventory.setItem(35, pagenext);
        }

        Player player = (Player) cs;
        player.openInventory(inventory);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() != null) {
            if (event.getView().getTitle().startsWith("§8Warps [Page ")) {
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getItemMeta() != null) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            Player player = (Player) event.getWhoClicked();
                            if (event.getCurrentItem().getType() == plugin.getVersion().getMaterial("SIGN")) {
                                Bukkit.dispatchCommand(player, "warp " + event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§6", ""));
                            }
                            if (event.getCurrentItem().getType() == Material.BOOK) {
                                Integer page = Integer.valueOf(event.getView().getTitle().split("Seite ")[1].split("]")[0]);
                                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Back")) {
                                    Bukkit.dispatchCommand(player, "warplist " + (page-1));
                                }
                                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Next")) {
                                    Bukkit.dispatchCommand(player, "warplist " + (page+1));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
