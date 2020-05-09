package me.velz.facility.commands;

import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        
        if(args.length != 2) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/pay <Player> <Amount>"));
            return true;
        }
        Bukkit.getServer().dispatchCommand(cs, "money pay " + args[0] + " " + args[1]);
        return false;
    }
    
    
    
}
