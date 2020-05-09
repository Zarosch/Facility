package me.velz.facility.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityKit;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreateKitCommand implements CommandExecutor {

    private final Facility plugin;

    public CreateKitCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.createkit")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if(args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/createkit <Kit>"));
            return true;
        }
        String kitname = args[0].toLowerCase();
        if (plugin.getKits().containsKey(kitname)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_ALREADYEXIST.getLocal());
            return true;
        }
        Player player = (Player) cs;
        final ArrayList<ItemStack> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                items.add(item);
            }
        }
        final FacilityKit facilityKit = new FacilityKit(kitname, "0", "facility.kit." + kitname, items);
        plugin.getKits().put(kitname, facilityKit);
        plugin.getFileManager().getKits().set("kits." + kitname + ".cooldown", "0");
        plugin.getFileManager().getKits().set("kits." + kitname + ".permission", "facility.kit." + kitname);
        Integer i = 1;
        for (ItemStack item : items) {
            plugin.getFileManager().getKits().set("kits." + kitname + ".items.item_" + i, item);
            i++;
        }
        try {
            plugin.getFileManager().getKits().getConfiguration().save(plugin.getFileManager().getKits().getFile());
        } catch (IOException ex) {
            Logger.getLogger(CreateKitCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_CREATE.getLocal().replaceAll("%kit", args[0]));
        return true;
    }

}
