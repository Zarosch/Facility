
package me.velz.facility.commands;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeleteKitCommand implements CommandExecutor {
    
    private final Facility plugin;

    public DeleteKitCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.deletekit")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if(args.length == 0) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/deletekit <Kit>"));
            return true;
        }
        String kitname = args[0].toLowerCase();
        if(!plugin.getKits().containsKey(kitname)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_NOTFOUND.getLocal());
            return true;
        }
        plugin.getKits().remove(kitname);
        plugin.getFileManager().getKits().getConfiguration().set("kits." + kitname, null);
        try {
            plugin.getFileManager().getKits().getConfiguration().save(plugin.getFileManager().getKits().getFile());
        } catch (IOException ex) {
            Logger.getLogger(DeleteKitCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_DELETE.getLocal().replaceAll("%kit", kitname));
        return true;
    }
    
    
    
}
