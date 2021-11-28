package com.jakubhekal.datausage.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.jakubhekal.datausage.R;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Calendar;

public class Utils {

    public static String convertFromBytes(long bytes) {
        if(bytes < 0) {
            return "0 B";
        }

        if (bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public static Long getDayStartMillis() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        c.add(Calendar.DAY_OF_MONTH, 0);

        return c.getTimeInMillis();
    }

    public static Long getPeriodEndMillis(int endingDay) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        while (c.get(Calendar.DAY_OF_MONTH) != endingDay - 1) {
            c.add(Calendar.DATE, 1);
        }

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

    public static String dateFromMillis(Long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);

        return c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR);
    }

    public static String formatDaysReaming(Context context, int days, String date) {
        if(days > 5 || days == 0) {
            return String.format(context.getString(R.string.period_info_main_3),days, date);
        } else if (days > 2) {
            return String.format(context.getString(R.string.period_info_main_2),days, date);
        } else {
            return String.format(context.getString(R.string.period_info_main_1),days, date);
        }
    }

    public static String getTheme(Context context){
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                return context.getString(R.string.dialog_theme_dark);

            case AppCompatDelegate.MODE_NIGHT_NO:
                return context.getString(R.string.dialog_theme_light);

            default:
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
            case AppCompatDelegate.MODE_NIGHT_UNSPECIFIED:
                return context.getString(R.string.dialog_theme_system);

        }
    }

    public static void setTheme(Context context, PreferenceManager preferenceManager, int mode, Class<?> cls){
        preferenceManager.setNightMode(mode);
        AppCompatDelegate.setDefaultNightMode(mode);
        Bundle bundle = ActivityOptions.makeCustomAnimation(context,
                android.R.anim.fade_in,android.R.anim.fade_out).toBundle();
        Intent intent = new Intent(context, cls);
        ((Activity) context).finish();
        context.startActivity(intent,bundle);
    }

    public static int getAttrColor(Context context, int attr){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }

    public static int getCurrentTheme(Context context) {
        PreferenceManager preferenceManager = new PreferenceManager(context);

        AppCompatDelegate.setDefaultNightMode(preferenceManager.getNightMode());

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return R.style.AppTheme_Dark;
        }

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
            int mode = uiModeManager.getNightMode();
            if (mode == UiModeManager.MODE_NIGHT_YES) {
                return R.style.AppTheme_Dark;
            }
        }

        return R.style.AppTheme;
    }

}
