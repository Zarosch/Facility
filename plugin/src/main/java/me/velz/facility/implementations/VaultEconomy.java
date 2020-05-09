package me.velz.facility.implementations;

import java.util.List;
import me.velz.facility.Facility;
import me.velz.facility.database.DatabasePlayer;
import me.velz.facility.utils.MessageUtil;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public class VaultEconomy implements Economy {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "FacilityEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double d) {
        return d + " " + currencyNamePlural();
    }

    @Override
    public String currencyNamePlural() {
        return MessageUtil.MONEYNAME.getLocal();
    }

    @Override
    public String currencyNameSingular() {
        return MessageUtil.MONEYNAME.getLocal();
    }

    @Override
    public boolean hasAccount(String playerName) {
        DatabasePlayer dbPlayer = Facility.getInstance().getDatabase().getUser(playerName);
        return dbPlayer.isSuccess();
    }

    @Override
    public boolean hasAccount(OfflinePlayer oPlayer) {
        return hasAccount(oPlayer.getUniqueId().toString());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer op, String string) {
        return hasAccount(op);
    }

    @Override
    public double getBalance(String playerName) {
        DatabasePlayer dbPlayer = Facility.getInstance().getDatabase().getUser(playerName);
        return dbPlayer.getMoney();
    }

    @Override
    public double getBalance(OfflinePlayer op) {
        return getBalance(op.getUniqueId().toString());
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer op, String worldName) {
        return getBalance(op);
    }

    @Override
    public boolean has(String playerName, double d) {
        return getBalance(playerName) >= d;
    }

    @Override
    public boolean has(OfflinePlayer op, double d) {
        return has(op.getName(), d);
    }

    @Override
    public boolean has(String playerName, String worldName, double d) {
        return has(playerName, d);
    }

    @Override
    public boolean has(OfflinePlayer op, String worldName, double d) {
        return has(op, d);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double d) {
        DatabasePlayer dbPlayer = Facility.getInstance().getDatabase().getUser(playerName);
        if(dbPlayer.isSuccess()) {
            dbPlayer.setMoney(dbPlayer.getMoney()-d);
            if(!dbPlayer.isOnline()){
                dbPlayer.save();
            }
            return new EconomyResponse(d, dbPlayer.getMoney(), EconomyResponse.ResponseType.SUCCESS, playerName);
        }
        return new EconomyResponse(d, 0, EconomyResponse.ResponseType.FAILURE, playerName);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer oPlayer, double d) {
        return withdrawPlayer(oPlayer.getName(), d);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double d) {
        return withdrawPlayer(playerName, d);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer oPlayer, String worldName, double d) {
        return withdrawPlayer(oPlayer.getName(), d);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double d) {
        DatabasePlayer dbPlayer = Facility.getInstance().getDatabase().getUser(playerName);
        if(dbPlayer.isSuccess()) {
            dbPlayer.setMoney(dbPlayer.getMoney()+d);
            if(!dbPlayer.isOnline()){
                dbPlayer.save();
            }
            return new EconomyResponse(d, dbPlayer.getMoney(), EconomyResponse.ResponseType.SUCCESS, playerName);
        }
        return new EconomyResponse(d, 0, EconomyResponse.ResponseType.FAILURE, playerName);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer oPlayer, double d) {
        return depositPlayer(oPlayer.getName(), d);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double d) {
        return depositPlayer(playerName, d);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer oPlayer, String worldName, double d) {
        return depositPlayer(oPlayer.getName(), d);
    }

    @Override
    public EconomyResponse createBank(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse createBank(String string, OfflinePlayer op) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse deleteBank(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse bankBalance(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EconomyResponse bankHas(String string, double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EconomyResponse bankWithdraw(String string, double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EconomyResponse bankDeposit(String string, double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EconomyResponse isBankOwner(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EconomyResponse isBankOwner(String string, OfflinePlayer op) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EconomyResponse isBankMember(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EconomyResponse isBankMember(String string, OfflinePlayer op) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String string) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer op) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String string, String string1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer op, String string) {
        return false;
    }

}
