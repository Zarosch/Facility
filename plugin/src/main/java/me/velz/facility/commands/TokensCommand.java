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

public class TokensCommand implements CommandExecutor {

    private final Facility plugin;

    public TokensCommand(Facility plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.hasPermission("facility.command.tokens")) {
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
            return true;
        }

        if (args.length == 0) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageUtil.PREFIX + MessageUtil.ERROR_PLAYERONLY.getLocal());
                return true;
            }

            final Player player = (Player) cs;
            final double money = plugin.getDatabase().getUser(player.getUniqueId().toString()).getToken();
            String moneyString = String.valueOf(money);
            if (moneyString.contains(".")) {
                String[] s = moneyString.split("\\.");
                if (s[1].equalsIgnoreCase("0")) {
                    moneyString = s[0];
                } else {
                    moneyString = moneyString.replaceAll("\\.", ",");
                }
            }
            player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_BALANCE_SELF.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", moneyString));
            return true;
            //<editor-fold defaultstate="collapsed" desc="/tokens help">
        } else if (args[0].equalsIgnoreCase("help")) {
            cs.sendMessage(" ");
            if (cs.hasPermission("facility.command.tokens.other")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_CHECK.getLocal());
            }
            if (cs.hasPermission("facility.command.tokens.pay")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_PAY.getLocal());
            }
            if (cs.hasPermission("facility.command.tokens.set")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_SET.getLocal());
            }
            if (cs.hasPermission("facility.command.tokens.give")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_GIVE.getLocal());
            }
            if (cs.hasPermission("facility.command.tokens.take")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_TAKE.getLocal());
            }
            if (cs.hasPermission("facility.command.tokens.giveall")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_GIVEALL.getLocal());
            }
            if (cs.hasPermission("facility.command.tokens.takeall")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_TAKEALL.getLocal());
            }
            if (cs.hasPermission("facility.command.tokens.top")) {
                cs.sendMessage(MessageUtil.TOKENS_HELP_TOP.getLocal());
            }
            cs.sendMessage(" ");
            return true;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/tokens pay">
        } else if (args[0].equalsIgnoreCase("pay")) {
            if (!cs.hasPermission("facility.command.tokens.pay")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tokens pay <player> <amount>"));
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
                    final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(player.getUniqueId().toString());
                    double money = dbPlayer.getToken();
                    if (money < d) {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_NOTENOUGH.getLocal());
                    } else {
                        money = money - d;
                        if (Bukkit.getPlayer(args[1]) == null) {
                            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                        } else {
                            final Player target = Bukkit.getPlayer(args[1]);
                            if (target == player) {
                                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_PAY_SELF.getLocal());
                            } else {
                                final DatabasePlayer dbTarget = plugin.getDatabase().getUser(target.getUniqueId().toString());
                                double targetMoney = dbTarget.getToken();
                                targetMoney = targetMoney + d;
                                dbPlayer.setToken(money);
                                dbTarget.setToken(targetMoney);
                                player.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_PAY_PLAYER.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%target", target.getName()));
                                target.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_PAY_TARGET.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", player.getName()));
                            }
                        }
                    }
                });
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/tokens top">
        } else if (args[0].equalsIgnoreCase("top")) {
            if (!cs.hasPermission("facility.command.tokens.top")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_BALANCETOP_HEADER.getLocal());
            int i = 1;
            final String query = "SELECT * FROM players ORDER BY token DESC LIMIT 20";
            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                connection = plugin.getDatabase().getConnection();
                ps = connection.prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    cs.sendMessage(MessageUtil.TOKENS_BALANCETOP_ENTRY.getLocal()
                            .replaceAll("%player", rs.getString("name"))
                            .replaceAll("%place", String.valueOf(i))
                            .replaceAll("%money", String.valueOf(rs.getInt("token")) + " " + MessageUtil.MONEYNAME.getLocal()));
                    i++;
                }
            } catch (SQLException ex) {
                Logger.getLogger(TokensCommand.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(TokensCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/tokens set">
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!cs.hasPermission("facility.command.tokens.set")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tokens set <player> <amount>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[1]);
                    if (dbPlayer.isSuccess()) {
                        dbPlayer.setToken(d);
                        if (!dbPlayer.isOnline()) {
                            dbPlayer.save();
                        }
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.MONEY_SET.getLocal().replaceAll("%moneyname", MessageUtil.MONEYNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", dbPlayer.getName()));
                    } else {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    }
                });
            } catch (NullPointerException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/tokens give">
        } else if (args[0].equalsIgnoreCase("give")) {
            if (!cs.hasPermission("facility.command.tokens.give")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tokens give <player> <amount>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[1]);
                    if (dbPlayer.isSuccess()) {
                        dbPlayer.setToken(dbPlayer.getToken() + d);
                        if (!dbPlayer.isOnline()) {
                            dbPlayer.save();
                        }
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_GIVE.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", args[1]));
                    } else {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    }
                });
            } catch (NullPointerException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/tokens take">
        } else if (args[0].equalsIgnoreCase("take")) {
            if (!cs.hasPermission("facility.command.tokens.take")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tokens take <player> <amount>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[1]);
                    if (dbPlayer.isSuccess()) {
                        dbPlayer.setToken(dbPlayer.getToken() - d);
                        if (!dbPlayer.isOnline()) {
                            dbPlayer.save();
                        }
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_TAKE.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)).replaceAll("%player", dbPlayer.getName()));
                    } else {
                        cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                    }
                });
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
                return true;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/tokens giveall">
        } else if (args[0].equalsIgnoreCase("giveall")) {
            if (!cs.hasPermission("facility.command.tokens.giveall")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tokens giveall <amount>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(all.getUniqueId().toString());
                        if (dbPlayer.isSuccess()) {
                            dbPlayer.setToken(dbPlayer.getToken() + d);
                            if (!dbPlayer.isOnline()) {
                                dbPlayer.save();
                            }
                            all.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_GIVEINFO.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
                        } else {
                            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                        }
                    }
                });
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_GIVEALL.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="/tokens takeall">
        } else if (args[0].equalsIgnoreCase("takeall")) {
            if (!cs.hasPermission("facility.command.tokens.takeall")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            if (args.length != 3) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_SYNTAX.getLocal().replaceAll("%command", "/tokens takeall <amount>"));
                return true;
            }
            String s = args[2];
            if (!s.contains(".")) {
                s = s + ".0";
            }
            try {
                final Double d = Double.valueOf(s);
                Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(all.getUniqueId().toString());
                        if (dbPlayer.isSuccess()) {
                            dbPlayer.setToken(dbPlayer.getToken() - d);
                            if (!dbPlayer.isOnline()) {
                                dbPlayer.save();
                            }
                            all.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_TAKEINFO.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
                        } else {
                            cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                        }
                    }
                });
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_GIVEALL.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(d)));
            } catch (NumberFormatException ex) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NONUMBER.getLocal());
            }
            //</editor-fold>
        } else {
            if (!cs.hasPermission("facility.command.tokens.other")) {
                cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_NOPERMISSIONS.getLocal());
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(Facility.getInstance(), () -> {
                final DatabasePlayer dbPlayer = plugin.getDatabase().getUser(args[0]);
                if (dbPlayer.isSuccess()) {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.TOKENS_BALANCE_OTHER.getLocal().replaceAll("%moneyname", MessageUtil.TOKENSNAME.getLocal()).replaceAll("%money", String.valueOf(dbPlayer.getToken())).replaceAll("%player", args[0]));
                } else {
                    cs.sendMessage(MessageUtil.PREFIX.getLocal() + MessageUtil.ERROR_PLAYERNOTFOUND.getLocal());
                }
            });
        }
        return true;
    }

}
