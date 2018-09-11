package me.velz.facility;

import me.velz.facility.database.DatabasePlayer;
import org.bukkit.entity.Player;

public class FacilityAPI {

    public static boolean hasTokens(Player player, Double tokens) {
        return Facility.getInstance().getPlayers().get(player.getUniqueId().toString()).getToken() >= tokens;
    }

    public static boolean hasMoney(Player player, Double money) {
        return Facility.getInstance().getPlayers().get(player.getUniqueId().toString()).getMoney() >= money;
    }

    public static void takeTokens(Player player, Double tokens) {
        DatabasePlayer dbPlayer = Facility.getInstance().getPlayers().get(player.getUniqueId().toString());
        dbPlayer.setToken(dbPlayer.getToken() - tokens);
    }

    public static void giveTokens(Player player, Double tokens) {
        DatabasePlayer dbPlayer = Facility.getInstance().getPlayers().get(player.getUniqueId().toString());
        dbPlayer.setToken(dbPlayer.getToken() + tokens);
    }

    public static void takeMoney(Player player, Double money) {
        DatabasePlayer dbPlayer = Facility.getInstance().getPlayers().get(player.getUniqueId().toString());
        dbPlayer.setMoney(dbPlayer.getMoney() - money);
    }

    public static void giveMoney(Player player, Double money) {
        DatabasePlayer dbPlayer = Facility.getInstance().getPlayers().get(player.getUniqueId().toString());
        dbPlayer.setMoney(dbPlayer.getMoney() + money);
    }

}
