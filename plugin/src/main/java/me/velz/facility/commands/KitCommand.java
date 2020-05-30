package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.objects.FacilityKit;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {

    private final Facility plugin;

    public KitCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.kit")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }
        if (args.length == 0) {
            if (plugin.getKits().isEmpty()) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_NOKITS.getLocal());
                return true;
            }
            String kits = "";
            for (String kit : plugin.getKits().keySet()) {
                kits = kits + kit + ", ";
            }
            kits = kits.substring(0, kits.length() - 2);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_LIST.getLocal().replaceAll("%kits", kits));
            return true;
        }
        String kitname = args[0].toLowerCase();
        if (!plugin.getKits().containsKey(kitname)) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_NOTFOUND.getLocal());
            return true;
        }
        final FacilityKit kit = plugin.getKits().get(kitname);
        if (args.length == 1) {
            Player player = (Player) cs;
            if (!kit.getCooldown().equalsIgnoreCase("0")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    if (plugin.getDatabase().issetKitCooldown(player.getUniqueId().toString(), kitname)) {
                        if (plugin.getDatabase().isKitCooldownExpired(player, kitname)) {
                            kit.give(player);
                            plugin.getDatabase().updateKitCooldown(player.getUniqueId().toString(), kitname, plugin.getTools().toMillis(kit.getCooldown()));
                        } else {
                            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.KIT_NOTREADY.getLocal());
                        }
                    } else {
                        kit.give(player);
                        plugin.getDatabase().insertKitCooldown(player.getUniqueId().toString(), kitname, plugin.getTools().toMillis(kit.getCooldown()));
                    }
                });
            } else {
                kit.give(player);
            }
            return true;
        }
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[1]) == null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            kit.give(target);
            return true;
        }
        return true;
    }

}
