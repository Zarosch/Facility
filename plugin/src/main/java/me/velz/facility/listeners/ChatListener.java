package me.velz.facility.listeners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.clip.placeholderapi.PlaceholderAPI;
import me.velz.facility.Facility;
import me.velz.facility.commands.GlobalMuteCommand;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener {

    private final Facility plugin;

    public ChatListener(Facility plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        //<editor-fold defaultstate="collapsed" desc="Is Globalmute on?">
        if (GlobalMuteCommand.isGlobalMute() && !event.getPlayer().hasPermission(plugin.getFileManager().getPermissionPrefix() + ".bypass.globalmute")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.CHAT_GLOBALMUTE_CANCEL.getLocal());
            return;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Chat Color">
        if (event.getPlayer().hasPermission(plugin.getFileManager().getPermissionPrefix() + ".color.chat")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Is player muted?">
        DatabasePlayer dbPlayer = plugin.getDatabase().getUser(event.getPlayer().getUniqueId().toString());
        if (!dbPlayer.getMute().equalsIgnoreCase("OK")) {
            event.setCancelled(true);
            final String[] data = dbPlayer.getMute().split(";");
            String duration;
            if (data[1].equalsIgnoreCase("-1")) {
                duration = "Permanent";
            } else {
                duration = plugin.getTools().getDateAsString(Long.valueOf(data[1]));
            }
            event.getPlayer().sendMessage(MessageUtil.PUNISH_MUTED_MESSAGE.getLocal().replaceAll("%reason", data[2]).replaceAll("%time", duration));
            return;
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Build Message">
        String message = ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getChatFormat()).replaceAll("%player", "%s");
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            message = message.replaceAll("%prefix", ChatColor.translateAlternateColorCodes('&', plugin.getImplementations().getVault().getChat().getPlayerPrefix(event.getPlayer())));
        } else {
            message = message.replaceAll("%prefix", "");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
        }
        String chatmessage = event.getMessage();
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Chat @Mention">
        if (plugin.getFileManager().isChatMention()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (chatmessage.contains("@" + player.getName())) {
                    chatmessage = chatmessage.replaceAll("@" + player.getName(), "§b@" + player.getName() + "§f");
                    player.playSound(player.getLocation(), plugin.getVersion().getSound("BLOCK_NOTE_BELL"), 1, 1);
                } else if (chatmessage.contains(player.getName())) {
                    chatmessage = chatmessage.replaceAll(player.getName(), "§b@" + player.getName() + "§f");
                    player.playSound(player.getLocation(), plugin.getVersion().getSound("BLOCK_NOTE_BELL"), 1, 1);
                }
            }
        }
        //</editor-fold>
        event.setMessage(chatmessage);
        event.setFormat(message.replaceAll("%message", "%s"));
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        ArrayList<String> args = new ArrayList();
        if (event.getMessage().split(" ").length > 1) {
            int i = 0;
            for (String arg : event.getMessage().split(" ")) {
                i++;
                if(i != 1) {
                    args.add(arg);
                }
            }
        }

        for (String currency : plugin.getCurrencies().keySet()) { 
            if (event.getMessage().startsWith("/" + currency + " ") || event.getMessage().equalsIgnoreCase("/" + currency)) {
                event.setCancelled(true);
                Player cs = event.getPlayer();

                if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency)) {
                    cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                    return;
                }

                if (args.isEmpty()) {
                    if (!(cs instanceof Player)) {
                        cs.sendMessage(MessageUtil.MONEY_PREFIX + MessageUtil.ERROR_PLAYERONLY.getLocal());
                        return;
                    }

                    final Player player = (Player) cs;
                    final double money = plugin.getDatabase().getUser(player.getUniqueId().toString()).getCurrencies().get(currency);
                    String moneyString = String.valueOf(money);
                    if (moneyString.contains(".")) {
                        String[] s = moneyString.split("\\.");
                        if (s[1].equalsIgnoreCase("0")) {
                            moneyString = s[0];
                        } else {
                            moneyString = moneyString.replaceAll("\\.", ",");
                        }
                    }
                    player.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_BALANCE_SELF.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", moneyString));
                    return;
                    //<editor-fold defaultstate="collapsed" desc="/money help">
                } else if (args.get(0).equalsIgnoreCase("help")) {
                    cs.sendMessage(" ");
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".other")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_CHECK.getLocal().replaceAll("%moneyname", currency));
                    }
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".pay")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_PAY.getLocal().replaceAll("%moneyname", currency));
                    }
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".set")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_SET.getLocal().replaceAll("%moneyname", currency));
                    }
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".give")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_GIVE.getLocal().replaceAll("%moneyname", currency));
                    }
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".take")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_TAKE.getLocal().replaceAll("%moneyname", currency));
                    }
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".giveall")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_GIVEALL.getLocal().replaceAll("%moneyname", currency));
                    }
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".takeall")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_TAKEALL.getLocal().replaceAll("%moneyname", currency));
                    }
                    if (cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".top")) {
                        cs.sendMessage(MessageUtil.MONEY_HELP_TOP.getLocal().replaceAll("%moneyname", currency));
                    }
                    cs.sendMessage(" ");
                    return;
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="/money pay">
                } else if (args.get(0).equalsIgnoreCase("pay")) {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".pay")) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    if (args.size() != 3) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/" + currency + " pay <player> <amount>"));
                        return;
                    }
                    final Player player = (Player) cs;
                    try {
                        String m = args.get(2);
                        if (!m.contains(".")) {
                            m = m + ".0";
                        }
                        final double d = Double.valueOf(m);
                        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(player.getUniqueId().toString());
                            double money = dbPlayer.getCurrencies().get(currency);
                            if (money < d) {
                                cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_NOTENOUGH.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()));
                            } else {
                                money = money - d;
                                if (Bukkit.getPlayer(args.get(1)) == null) {
                                    cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                                } else {
                                    final Player target = Bukkit.getPlayer(args.get(1));
                                    if (target == player) {
                                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_PAY_SELF.getLocal());
                                    } else {
                                        final DatabasePlayer dbTarget = plugin.getDatabase().getUser(target.getUniqueId().toString());
                                        double targetMoney = dbTarget.getCurrencies().get(currency);
                                        targetMoney = targetMoney + d;
                                        dbPlayer.getCurrencies().put(currency, money);
                                        dbTarget.getCurrencies().put(currency, targetMoney);
                                        player.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_PAY_PLAYER.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)).replaceAll("%target", target.getName()));
                                        target.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_PAY_TARGET.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", player.getName()));
                                    }
                                }
                            }
                        });
                    } catch (NumberFormatException ex) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NONUMBER.getLocal());
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="/money top">
                } else if (args.get(0).equalsIgnoreCase("top")) {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".top")) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_BALANCETOP_HEADER.getLocal());
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        HashMap<Integer, HashMap<String, Double>> rangs = plugin.getDatabase().currencyToplist(currency);
                        for(Integer rang : rangs.keySet()) {
                            for(String player : rangs.get(rang).keySet()) {
                                Double balance = rangs.get(rang).get(player);
                                cs.sendMessage(MessageUtil.MONEY_BALANCETOP_ENTRY.getLocal()
                                        .replaceAll("%player", player)
                                        .replaceAll("%place", String.valueOf(rang))
                                        .replaceAll("%money", String.valueOf(balance) + " " + plugin.getCurrencies().get(currency).getDisplayName()));
                            }
                        }
                    });
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="/money set">
                } else if (args.get(0).equalsIgnoreCase("set")) {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".set")) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    if (args.size() != 3) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/" + currency + " set <player> <amount>"));
                        return;
                    }
                    String s = args.get(2);
                    if (!s.contains(".")) {
                        s = s + ".0";
                    }
                    try {
                        final Double d = Double.valueOf(s);
                        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args.get(1));
                            if (dbPlayer.isSuccess()) {
                                dbPlayer.getCurrencies().put(currency, d);
                                if (!dbPlayer.isOnline()) {
                                    dbPlayer.save();
                                }
                                cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_SET.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", dbPlayer.getName()));
                            } else {
                                cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                            }
                        });
                    } catch (NullPointerException ex) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NONUMBER.getLocal());
                        return;
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="/money give">
                } else if (args.get(0).equalsIgnoreCase("give")) {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".give")) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    if (args.size() != 3) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/" + currency + " give <player> <amount>"));
                        return;
                    }
                    String s = args.get(2);
                    if (!s.contains(".")) {
                        s = s + ".0";
                    }
                    try {
                        final Double d = Double.valueOf(s);
                        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args.get(1));
                            if (dbPlayer.isSuccess()) {
                                dbPlayer.getCurrencies().put(currency, dbPlayer.getCurrencies().get(currency) + d);
                                if (!dbPlayer.isOnline()) {
                                    dbPlayer.save();
                                }
                                cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_GIVE.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", args.get(1)));
                            } else {
                                cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                            }
                        });
                    } catch (NullPointerException ex) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NONUMBER.getLocal());
                        return;
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="/money take">
                } else if (args.get(0).equalsIgnoreCase("take")) {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".take")) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    if (args.size() != 3) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/" + currency + " take <player> <amount>"));
                        return;
                    }
                    String s = args.get(2);
                    if (!s.contains(".")) {
                        s = s + ".0";
                    }
                    try {
                        final Double d = Double.valueOf(s);
                        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                            final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args.get(1));
                            if (dbPlayer.isSuccess()) {
                                dbPlayer.getCurrencies().put(currency, dbPlayer.getCurrencies().get(currency) - d);
                                if (!dbPlayer.isOnline()) {
                                    dbPlayer.save();
                                }
                                cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_TAKE.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", dbPlayer.getName()));
                            } else {
                                cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                            }
                        });
                    } catch (NumberFormatException ex) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NONUMBER.getLocal());
                        return;
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="/money giveall">
                } else if (args.get(0).equalsIgnoreCase("giveall")) {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".giveall")) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    if (args.size() != 2) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/" + currency + " giveall <amount>"));
                        return;
                    }
                    String s = args.get(1);
                    if (!s.contains(".")) {
                        s = s + ".0";
                    }
                    try {
                        final Double d = Double.valueOf(s);
                        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                            Bukkit.getOnlinePlayers().forEach((all) -> {
                                final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(all.getUniqueId().toString());
                                if (dbPlayer.isSuccess()) {
                                    dbPlayer.getCurrencies().put(currency, dbPlayer.getCurrencies().get(currency) + d);
                                    if (!dbPlayer.isOnline()) {
                                        dbPlayer.save();
                                    }
                                    all.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_GIVEINFO.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)));
                                } else {
                                    cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                                }
                            });
                        });
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_GIVEALL.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)));
                    } catch (NumberFormatException ex) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NONUMBER.getLocal());
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="/money takeall">
                } else if (args.get(0).equalsIgnoreCase("takeall")) {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".takeall")) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    if (args.size() != 2) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/" + currency + " takeall <amount>"));
                        return;
                    }
                    String s = args.get(1);
                    if (!s.contains(".")) {
                        s = s + ".0";
                    }
                    try {
                        final Double d = Double.valueOf(s);
                        Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                            Bukkit.getOnlinePlayers().forEach((all) -> {
                                final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(all.getUniqueId().toString());
                                if (dbPlayer.isSuccess()) {
                                    dbPlayer.getCurrencies().put(currency, dbPlayer.getCurrencies().get(currency) - d);
                                    if (!dbPlayer.isOnline()) {
                                        dbPlayer.save();
                                    }
                                    all.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_TAKEINFO.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)));
                                } else {
                                    cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                                }
                            });
                        });
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_GIVEALL.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(d)));
                    } catch (NumberFormatException ex) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NONUMBER.getLocal());
                    }
                    //</editor-fold>
                } else {
                    if (!cs.hasPermission(plugin.getFileManager().getPermissionPrefix() + ".command.currencies." + currency + ".other")) {
                        cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                        return;
                    }
                    Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                        final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args.get(0));
                        if (dbPlayer.isSuccess()) {
                            cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.MONEY_BALANCE_OTHER.getLocal().replaceAll("%moneyname", plugin.getCurrencies().get(currency).getDisplayName()).replaceAll("%money", String.valueOf(dbPlayer.getCurrencies().get(currency))).replaceAll("%player", args.get(0)));
                        } else {
                            cs.sendMessage(plugin.getCurrencies().get(currency).getPrefix() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                        }
                    });
                }
            }
        }
    }
}
