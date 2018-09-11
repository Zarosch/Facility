package me.velz.facility.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class Tools {

    public String getPlaytime(Integer value) {
        Integer minutes = value / 60;
        Integer hours = value / 60 / 60;
        if (hours != 0) {
            minutes = minutes - hours * 60;
        }
        String minu, hour;
        if (minutes != 1) {
            minu = minutes + " " + MessageUtil.PLAYTIME_MINUTES.getLocal();
        } else {
            minu = minutes + " " + MessageUtil.PLAYTIME_MINUTE.getLocal();
        }
        if (hours != 1) {
            hour = hours + " " + MessageUtil.PLAYTIME_HOURS.getLocal();
        } else {
            hour = hours + " " + MessageUtil.PLAYTIME_HOUR.getLocal();
        }
        return hour + " " + minu;
    }

    public String getPlaytimeShort(Integer value) {
        Integer minutes = value / 60;
        Integer hours = value / 60 / 60;
        if (hours != 0) {
            minutes = minutes - hours * 60;
        }
        String minu, hour;
        minu = minutes + MessageUtil.PLAYTIME_M.getLocal();
        if (hours != 0) {
            hour = hours + MessageUtil.PLAYTIME_H.getLocal();
            return hour + " " + minu;
        } else {
            return minu;
        }
    }

    public String getPlaytimeF(Integer value) {
        Integer minutes = value / 60;
        Integer hours = value / 60 / 60;
        if (hours != 0) {
            minutes = minutes - hours * 60;
        }
        String minu, hour;
        if (hours == 0) {
            if (minutes != 1) {
                return minutes + " " + MessageUtil.PLAYTIME_MINUTES.getLocal();
            } else {
                return minutes + " " + MessageUtil.PLAYTIME_MINUTE.getLocal();
            }
        } else if (hours != 1) {
            return hours + " " + MessageUtil.PLAYTIME_HOURS.getLocal();
        } else {
            return hours + " " + MessageUtil.PLAYTIME_HOUR.getLocal();
        }
    }

    public String getDate(Long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm", Locale.GERMANY);
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }

    public Integer toMillis(String time) {
        Integer i = Integer.valueOf(time.substring(0, time.length() - 1));
        if (time.endsWith("s")) {
            return i * 1000;
        }
        if (time.endsWith("m")) {
            return i * 60000;
        }
        if (time.endsWith("h")) {
            return i * 60 * 60000;
        }
        if (time.endsWith("d")) {
            return i * 60 * 24 * 60000;
        }
        if (time.endsWith("d")) {
            return i * 60 * 24 * 60000;
        }
        if (time.endsWith("w")) {
            return i * 60 * 24 * 7 * 60000;
        }
        return null;
    }

    public String getDateAsString(Long milis) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(milis));
    }

    public String getPlaytimeDigis(Integer value) {
        Integer minutes = value / 60;
        Integer hours = value / 60 / 60;
        if (hours != 0) {
            minutes = minutes - hours * 60;
        }
        String minu, hour;
        if (minutes <= 9) {
            minu = "0" + minutes;
        } else {
            minu = String.valueOf(minutes);
        }
        if (hours <= 9) {
            hour = "0" + hours;
        } else {
            hour = String.valueOf(hours);
        }
        return hour + ":" + minu;
    }

}
