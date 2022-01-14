package com.jakubhekal.datausage.managers;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Process;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.jakubhekal.datausage.activities.MainActivity;

import static android.content.Context.POWER_SERVICE;

public class PermissionManager {

    public static boolean hasPermissions(Context context) {
        return hasPermissionToReadNetworkHistory(context) && hasPermissionToIgnoreBatteryOptimizations(context) && hasPermissionToReadPhoneStats(context);
    }

    public static boolean hasPermissionToReadPhoneStats(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasPermissionToIgnoreBatteryOptimizations(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
        return pm.isIgnoringBatteryOptimizations(context.getPackageName());
    }

    public static boolean hasPermissionToReadNetworkHistory(Context context) {
        final AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(), context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    public static void requestPermissions(Context context) {
        if(!hasPermissionToReadNetworkHistory(context)) {
            requestPermissionToReadNetworkHistory(context);
        }

        if(!hasPermissionToReadPhoneStats(context)) {
            requestPermissionToReadPhoneStats(context);
        }

        if(!hasPermissionToIgnoreBatteryOptimizations(context)) {
            requestPermissionToIgnoreBatteryOptimizations(context);
        }

    }

    public static void requestPermissionToReadPhoneStats(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 37);
    }

    public static void requestPermissionToIgnoreBatteryOptimizations(Context context) {
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:"+context.getPackageName()));
        context.startActivity(intent);
    }

    public static void requestPermissionToReadNetworkHistory(Context context) {
        requestPermissionToReadNetworkHistory(context, true);
    }

    public static void requestPermissionToReadNetworkHistory(Context context, boolean includeData) {
        try {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            if(includeData) {
                intent.setData(Uri.parse("package:" + context.getPackageName()));
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            if(includeData) {
                requestPermissionToReadNetworkHistory(context, false);
            } else {
                Toast.makeText(context, "Unable to request permission", Toast.LENGTH_LONG);
            }
        }

    }

}
