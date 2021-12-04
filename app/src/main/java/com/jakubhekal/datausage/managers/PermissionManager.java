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
            mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(), context.getPackageName());
        }
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                context.getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                Process.myUid(), context.getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
                        Intent intent = new Intent(context, MainActivity.class);
                        if (intent.getExtras() != null) {
                            intent.putExtras(intent.getExtras());
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
        return false;
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
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        context.startActivity(intent);
    }

}
