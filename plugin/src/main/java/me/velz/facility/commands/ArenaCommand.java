package me.velz.facility.commands;

import me.velz.facility.Facility;
import me.velz.facility.arenas.FacilityArena;
import me.velz.facility.arenas.FacilityArenaMode;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    private final Facility plugin;

    public ArenaCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        
        if(args.length == 0) {
            cs.sendMessage("");
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_JOIN.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_LEAVE.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_KICK.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_ADD.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_REMOVE.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_LIST.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_ADDMODE.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_REMOVEMODE.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_SETMODE.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_LISTMODES.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_ADDSETTINGS.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_RMSETTINGS.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_LISTSETTINGS.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_ADDLOC.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_REMLOC.getLocal());
            cs.sendMessage(MessageUtil.ARENA_COMMAND_HELP_LISTLOCS.getLocal());
            cs.sendMessage("");
            return true;
        }
        
        if(args[0].equalsIgnoreCase("join")) {
            if(!cs.hasPermission("arena.command.join")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a join <Arena>"));
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                return true;
            }
            plugin.getArenaManager().getArenas().get(args[1]).join((Player)cs);
            return true;
        }
        
        if(args[0].equalsIgnoreCase("leave")) {
            if(!cs.hasPermission("arena.command.leave")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            Player player = (Player)cs;
            if(!player.hasMetadata("arena")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOTIN_SELF.getLocal());
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(player.getMetadata("arena").get(0).asString())) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOTIN_SELF.getLocal());
                return true;
            }
            FacilityArena arena = plugin.getArenaManager().getArenas().get(player.getMetadata("arena").get(0).asString());
            arena.leave(player);
            return true;
        }
        
        if(args[0].equalsIgnoreCase("kick")) {
            if(!cs.hasPermission("arena.command.kick")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a kick <Player>"));
                return true;
            }
            if(Bukkit.getPlayer(args[1]) != null) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if(!target.hasMetadata("arena")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOTIN_OTHER.getLocal());
                return true;
            }
            FacilityArena arena = plugin.getArenaManager().getArenas().get(target.getMetadata("arena").get(0).asString());
            target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_KICK.getLocal().replaceAll("%arena", arena.getId()));
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_KICK.getLocal().replaceAll("%arena", arena.getId()).replaceAll("%player", target.getName()));
            arena.leave(target);
            return true;
        }
        
        if(args[0].equalsIgnoreCase("add")) {
            if(!cs.hasPermission("arena.command.add")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a add <Arena>"));
                return true;
            }
            if(plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_EXISTS.getLocal());
                return true;
            }
            plugin.getArenaManager().addArena(args[1]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_ADD.getLocal().replaceAll("%arena", args[1]));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("remove")) {
            if(!cs.hasPermission("arena.command.remove")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a remove <Arena>"));
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                return true;
            }
            plugin.getArenaManager().removeArena(args[1]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_REMOVE.getLocal().replaceAll("%arena", args[1]));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("list")) {
            if(!cs.hasPermission("arena.command.list")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_LIST.getLocal().replaceAll("%arenas", plugin.getArenaManager().getArenaList()));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("addmode")) {
            if(!cs.hasPermission("arena.command.addmode")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a addmode <Mode>"));
                return true;
            }
            if(plugin.getArenaManager().getModes().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_MODE_EXISTS.getLocal());
                return true;
            }
            plugin.getArenaManager().addMode(args[1]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_ADDMODE.getLocal().replaceAll("%mode", args[1]));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("remmode")) {
            if(!cs.hasPermission("arena.command.remmode")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a remmode <Mode>"));
                return true;
            }
            if(!plugin.getArenaManager().getModes().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_MODE_NOT_FOUND.getLocal());
                return true;
            }
            plugin.getArenaManager().removeMode(args[1]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_REMOVEMODE.getLocal().replaceAll("%mode", args[1]));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("setmode")) {
            if(!cs.hasPermission("arena.command.remove")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a setmode <Arena> <Modus>"));
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                return true;
            }
            if(!plugin.getArenaManager().getModes().containsKey(args[2])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_MODE_NOT_FOUND.getLocal());
                return true;
            }
            plugin.getArenaManager().setMode(args[1], args[2]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_SETMODE.getLocal().replaceAll("%arena", args[1]).replaceAll("%mode", args[2]));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("listmodes")) {
            if(!cs.hasPermission("arena.command.listmodes")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_LISTMODES.getLocal().replaceAll("%modes", plugin.getArenaManager().getModeList()));
            return true;
        }        
        if(args[0].equalsIgnoreCase("addloc")) {
            if(!cs.hasPermission("arena.command.addloc")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            if(args.length < 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a addloc <Arena> <Location>"));
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                return true;
            }
            Player player = (Player)cs;
            plugin.getArenaManager().addLocation(args[1], args[2], player.getLocation());
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_ADDLOC.getLocal().replaceAll("%arena", args[1]).replaceAll("%loc", args[2]));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("remloc")) {
            if(!cs.hasPermission("arena.command.remloc")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }
            if(args.length < 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a remloc <Arena> <Location>"));
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                return true;
            }
            if(!plugin.getArenaManager().getArenas().get(args[1]).getLocations().containsKey(args[2])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_LOC_NOT_FOUND);
                return true;
            }
            plugin.getArenaManager().removeLocation(args[1], args[2]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_REMOVELOC.getLocal().replaceAll("%arena", args[1]).replaceAll("%loc", args[2]));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("listlocs")) {
            if(!cs.hasPermission("arena.command.listlocs")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a listlocs <Arena>"));
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                return true;
            }
            FacilityArena arena = plugin.getArenaManager().getArenas().get(args[1]);
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_LISTLOCS.getLocal().replaceAll("%locs", plugin.getArenaManager().getLocationList(arena)));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("addsetting")) {
            if(!cs.hasPermission("arena.command.addsetting")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 4) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a addsetting <mode/arena> <target> <setting>"));
                return true;
            }
            if(args[1].equalsIgnoreCase("arena")) {
                if(!plugin.getArenaManager().getArenas().containsKey(args[2])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                    return true;
                }
                FacilityArena arena = plugin.getArenaManager().getArenas().get(args[2]);
                if(arena.getSettings().contains(args[3])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_ADDSETTING_ALREADY.getLocal());
                    return true;
                }
                plugin.getArenaManager().addSetting(args[2], args[1], args[3]);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_ADDSETTING_SET.getLocal().replaceAll("%target", args[1]).replaceAll("%setting", args[3]));
            }
            if(args[1].equalsIgnoreCase("mode")) {
                if(!plugin.getArenaManager().getModes().containsKey(args[2])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_MODE_NOT_FOUND.getLocal());
                    return true;
                }
                FacilityArenaMode mode = plugin.getArenaManager().getModes().get(args[2]);
                if(mode.getSettings().contains(args[3])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_ADDSETTING_ALREADY.getLocal());
                    return true;
                }
                plugin.getArenaManager().addSetting(args[2], args[1], args[3]);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_ADDSETTING_SET.getLocal().replaceAll("%target", args[1]).replaceAll("%setting", args[3]));
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a addsetting <mode/arena> <target> <setting>"));
            return true;
        }
        
        if(args[0].equalsIgnoreCase("remsetting")) {
            if(!cs.hasPermission("arena.command.remsetting")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS);
                return true;
            }
            if(args.length < 4) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a remsetting <mode/arena> <target> <setting>"));
                return true;
            }
            if(args[1].equalsIgnoreCase("arena")) {
                if(!plugin.getArenaManager().getArenas().containsKey(args[2])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                    return true;
                }
                FacilityArena arena = plugin.getArenaManager().getArenas().get(args[2]);
                if(arena.getSettings().contains(args[3])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_REMSETTING_NOTSET.getLocal().replaceAll("%target", args[1]).replaceAll("%setting", args[3]));
                    return true;
                }
                plugin.getArenaManager().addSetting(args[2], args[1], args[3]);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_REMSETTING_REMOVED.getLocal().replaceAll("%target", args[1]).replaceAll("%setting", args[3]));
            }
            if(args[1].equalsIgnoreCase("mode")) {
                if(!plugin.getArenaManager().getModes().containsKey(args[2])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_MODE_NOT_FOUND.getLocal());
                    return true;
                }
                FacilityArenaMode mode = plugin.getArenaManager().getModes().get(args[2]);
                if(mode.getSettings().contains(args[3])) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_REMSETTING_NOTSET.getLocal().replaceAll("%target", args[1]).replaceAll("%setting", args[3]));
                    return true;
                }
                plugin.getArenaManager().addSetting(args[2], args[1], args[3]);
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_REMSETTING_REMOVED.getLocal().replaceAll("%target", args[1]).replaceAll("%setting", args[3]));
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a remsetting <mode/arena> <target> <setting>"));
        }
        
        if(args[0].equalsIgnoreCase("listsettings")) {
            if(!cs.hasPermission("arena.command.listsettings")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if(args.length < 2) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/a listsettings <Arena>"));
                return true;
            }
            if(!plugin.getArenaManager().getArenas().containsKey(args[1])) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_NOT_FOUND.getLocal());
                return true;
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ARENA_COMMAND_LISTSETTINGS.getLocal().replaceAll("%settings", plugin.getArenaManager().listSettings(args[1])));
            return true;
        }
                
        return true;
    }

    
    
}
