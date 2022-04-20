package com.jakubhekal.datausage;

import android.util.Log;

import com.jakubhekal.datausage.managers.PreferenceManager;

import java.util.Calendar;

public class DateTimeUtils {
    public static Long getDayStartMillis() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        c.add(Calendar.DAY_OF_MONTH, 0);

        return c.getTimeInMillis();
    }

    public static Long getDayEndMillis() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        c.add(Calendar.DAY_OF_MONTH, 1);

        return c.getTimeInMillis();
    }


    public static Long getPeriodEndMillis(int endingDay) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        do {
            c.add(Calendar.DATE, 1);
        } while (c.get(Calendar.DAY_OF_MONTH) != endingDay);

        c.add(Calendar.DATE, -1);

        return c.getTimeInMillis();
    }

    public static Long getPeriodStartMillis(int startingDay) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        while (c.get(Calendar.DAY_OF_MONTH) != startingDay) {
            c.add(Calendar.DATE, -1);
        }

        return c.getTimeInMillis();
    }

    public static int getDaysTillPeriodEnd(PreferenceManager preferenceManager) {
        return (int) (((DateTimeUtils.getPeriodEndMillis(preferenceManager.getPeriodStart()) - DateTimeUtils.getDayStartMillis()) / (1000*60*60*24)) + 1);
    }

    public static String dateFromMillis(Long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);

        return c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);
    }
}
