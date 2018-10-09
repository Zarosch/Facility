package me.velz.facility.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    private final Facility plugin;

    public MoneyCommand(Facility plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.money")) {
            cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (args.length == 0) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }

            final Player player = (Player) cs;
            final double money = plugin.getMysqlDatabase().getUser(player.getUniqueId().toString()).getMoney();
            String moneyString = String.valueOf(money);
            if (moneyString.contains(".")) {
                String[] s = moneyString.split("\\.");
                if (s[1].equalsIgnoreCase("0")) {
                    moneyString = s[0];
                } else {
                    moneyString = moneyString.replaceAll("\\.", ",");
                }
            }
            player.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_BALANCE_SELF.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", moneyString));
            return true;
            //<editor-fold defaultstate="collapsed" desc="/money help">
        } else if (args[0].equalsIgnoreCase("help")) {
            cs.sendMessage(" ");
            if (cs.hasPermission("facility.command.money.other")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_CHECK.getLocal());
            }
            if (cs.hasPermission("facility.command.money.pay")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_PAY.getLocal());
            }
            if (cs.hasPermission("facility.command.money.set")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_SET.getLocal());
            }
            if (cs.hasPermission("facility.command.money.give")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_GIVE.getLocal());
            }
            if (cs.hasPermission("facility.command.money.take")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_TAKE.getLocal());
            }
            if (cs.hasPermission("facility.command.money.giveall")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_GIVEALL.getLocal());
            }
            if (cs.hasPermission("facility.command.money.takeall")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_TAKEALL.getLocal());
            }
            if (cs.hasPermission("facility.command.money.top")) {
                cs.sendMessage(MessageUtil.MONEY_HELP_TOP.getLocal());
            }
            cs.sendMessage(" ");
            return true;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/money pay">
        } else if (args[0].equalsIgnoreCase("pay")) {
            if (!cs.hasPermission("facility.command.money.pay")) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/money pay <player> <amount>"));
                return true;
            }
            final Player player = (Player) cs;
            try {
                String m = args[2];
                if (!m.contains(".")) {
                    m = m + ".0";
                }
                final double d = Double.valueOf(m);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(player.getUniqueId().toString());
                    double money = dbPlayer.getMoney();
                    if (money < d) {
                        cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_NOTENOUGH.getLocal());
                    } else {
                        money = money - d;
                        if (Bukkit.getPlayer(args[1]) == null) {
                            cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                        } else {
                            final Player target = Bukkit.getPlayer(args[1]);
                            if (target == player) {
                                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_PAY_SELF.getLocal());
                            } else {
                                final DatabasePlayer dbTarget = plugin.getMysqlDatabase().getUser(target.getUniqueId().toString());
                                double targetMoney = dbTarget.getMoney();
                                targetMoney = targetMoney + d;
                                dbPlayer.setMoney(money);
                                dbTarget.setMoney(targetMoney);
                                player.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_PAY_PLAYER.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%target", target.getName()));
                                target.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_PAY_TARGET.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", player.getName()));
                            }
                        }
                    }
                });
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/money top">
        } else if (args[0].equalsIgnoreCase("top")) {
            if (!cs.hasPermission("facility.command.money.top")) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_BALANCETOP_HEADER.getLocal());
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    int i = 1;

                    final String query = "SELECT * FROM " + plugin.getMysqlDatabase().getPrefix() + "players ORDER BY money DESC LIMIT 20";
                    Connection connection = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    try {
                        connection = plugin.getMysqlDatabase().getHikari().getConnection();
                        ps = connection.prepareStatement(query);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            cs.sendMessage(MessageUtil.MONEY_BALANCETOP_ENTRY.getLocal()
                                    .replaceAll("%player", rs.getString("name"))
                                    .replaceAll("%place", String.valueOf(i))
                                    .replaceAll("%money", String.valueOf(rs.getInt("money")) + " " + MessageUtil.MONEYNAME.getLocal()));
                            i++;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(MoneyCommand.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            if (connection != null) {
                                connection.close();
                            }
                            if (ps != null) {
                                ps.close();
                            }
                            if (rs != null) {
                                rs.close();
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(MoneyCommand.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/money set">
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!cs.hasPermission("facility.command.money.set")) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/money set <Player> <Betrag>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(args[1]);
                    if (dbPlayer.isSuccess()) {
                        dbPlayer.setMoney(d);
                        if (!dbPlayer.isOnline()) {
                            dbPlayer.save();
                        }
                        cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_SET.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", dbPlayer.getName()));
                    } else {
                        cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    }
                });
            } catch (NullPointerException ex) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/money give">
        } else if (args[0].equalsIgnoreCase("give")) {
            if (!cs.hasPermission("facility.command.money.give")) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/money give <Player> <Betrag>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(args[1]);
                    if (dbPlayer.isSuccess()) {
                        dbPlayer.setMoney(dbPlayer.getMoney() + d);
                        if (!dbPlayer.isOnline()) {
                            System.out.println("ONLINE SAVE");
                            dbPlayer.save();
                        }
                        cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_GIVE.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", args[1]));
                    } else {
                        cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    }
                });
            } catch (NullPointerException ex) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/money take">
        } else if (args[0].equalsIgnoreCase("take")) {
            if (!cs.hasPermission("facility.command.money.take")) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/money take <Player> <Betrag>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(args[1]);
                    if (dbPlayer.isSuccess()) {
                        dbPlayer.setMoney(dbPlayer.getMoney() - d);
                        if (!dbPlayer.isOnline()) {
                            dbPlayer.save();
                        }
                        cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_TAKE.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", dbPlayer.getName()));
                    } else {
                        cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    }
                });
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/money giveall">
        } else if (args[0].equalsIgnoreCase("giveall")) {
            if (!cs.hasPermission("facility.command.money.giveall")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/money giveall <Betrag>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    Bukkit.getOnlinePlayers().forEach((all) -> {
                        final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(all.getUniqueId().toString());
                        if (dbPlayer.isSuccess()) {
                            dbPlayer.setMoney(dbPlayer.getMoney() + d);
                            if (!dbPlayer.isOnline()) {
                                dbPlayer.save();
                            }
                            all.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_GIVEINFO.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
                        } else {
                            cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                        }
                    });
                });
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_GIVEALL.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/money takeall">
        } else if (args[0].equalsIgnoreCase("takeall")) {
            if (!cs.hasPermission("facility.command.money.takeall")) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/money takeall <Betrag>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    Bukkit.getOnlinePlayers().forEach((all) -> {
                        final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(all.getUniqueId().toString());
                        if (dbPlayer.isSuccess()) {
                            dbPlayer.setMoney(dbPlayer.getMoney() - d);
                            if (!dbPlayer.isOnline()) {
                                dbPlayer.save();
                            }
                            all.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_TAKEINFO.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
                        } else {
                            cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                        }
                    });
                });
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_GIVEALL.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            }
            //</editor-fold>
        } else {
            if (!cs.hasPermission("facility.command.money.other")) {
                cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                final DatabasePlayer dbPlayer = plugin.getMysqlDatabase().getUser(args[0]);
                if (dbPlayer.isSuccess()) {
                    cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.MONEY_BALANCE_OTHER.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(dbPlayer.getMoney())).replaceAll("%player", args[0]));
                } else {
                    cs.sendMessage(MessageUtil.MONEY_PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                }
            });
        }
        return true;
    }

}
