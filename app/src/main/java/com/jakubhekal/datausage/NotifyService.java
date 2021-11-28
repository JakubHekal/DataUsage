
package com.jakubhekal.datausage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.NetworkType;

import com.jakubhekal.datausage.utils.NetworkStatsHelper;
import com.jakubhekal.datausage.utils.PreferenceManager;
import com.jakubhekal.datausage.utils.Utils;


public class NotifyService extends Service {

    private static final int NOTIFICATION_PERMANENT_ID = 1;
    private static final String NOTIFICATION_PERMANENT_CHANNEL = "datausage.permanent";
    private static final int NOTIFICATION_USAGE_WARNING_ID = 2;
    private static final String NOTIFICATION_USAGE_WARNING_CHANNEL = "datausage.warning";
    private static final int HANDLER_DELAY = 2000;

    Handler handler;
    NotificationManager notificationManager;
    ConnectivityManager connectivityManager;
    PreferenceManager preferenceManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationManager = getSystemService(NotificationManager.class);
        connectivityManager = getSystemService(ConnectivityManager.class);
        handler = new Handler(Looper.getMainLooper());
        preferenceManager = new PreferenceManager(getApplicationContext());

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager);

        NotificationChannel permanent_channel = new NotificationChannel(NOTIFICATION_PERMANENT_CHANNEL, getString(R.string.permanent_title), NotificationManager.IMPORTANCE_LOW);
        permanent_channel.setDescription(getString(R.string.permanent_info));
        permanent_channel.setShowBadge(false);
        notificationManager.createNotificationChannel(permanent_channel);

        NotificationChannel warning_channel = new NotificationChannel(NOTIFICATION_USAGE_WARNING_CHANNEL, getString(R.string.warning_title), NotificationManager.IMPORTANCE_DEFAULT);
        warning_channel.setDescription(getString(R.string.warning_info));
        warning_channel.setVibrationPattern(new long[] {1000});
        notificationManager.createNotificationChannel(warning_channel);

        handler.postDelayed(new Runnable() {
            public void run() {
                preferenceManager.reload();
                boolean isMobileDataConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE;
                int daysTillEndOfPeriod = (int) ((Utils.getPeriodEndMillis(preferenceManager.getPeriodStart()) - Utils.getDayStartMillis()) / (1000*60*60*24)) + 1;
                Long periodUsage = networkStatsHelper.getAllBytesMobile(getApplicationContext(), Utils.getPeriodStartMillis(preferenceManager.getPeriodStart()));
                Long periodLimit = preferenceManager.getPeriodLimit();
                Long dailyUsage = networkStatsHelper.getAllBytesMobile(getApplicationContext(), Utils.getDayStartMillis());
                Long dailyLimit = preferenceManager.getDailyLimitCustom() ? preferenceManager.getDailyLimit() : (periodLimit - (periodUsage-dailyUsage)) / daysTillEndOfPeriod;
                float dailyPercentage = ((float)periodUsage / periodLimit);

                if(preferenceManager.getNotificationPermanent()
                ) {
                    showPermanentNotification(Utils.convertFromBytes(dailyUsage), Utils.convertFromBytes(dailyLimit));
                } else {
                    killContinuousNotification();
                }

                handler.postDelayed(this, HANDLER_DELAY);
            }
        }, HANDLER_DELAY);

        return START_STICKY;
    }

    private void showPermanentNotification(String usage, String limit) {
        Intent appIntent = new Intent(getApplicationContext(), SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_PERMANENT_CHANNEL)
                .setSmallIcon(R.drawable.icon_data_usage)
                .setColor(getColor(R.color.lightPrimary))
                .setContentTitle(String.format(getString(R.string.notification_permanent_title), usage))
                .setContentText(String.format(getString(R.string.notification_permanent_info), limit))
                .setSilent(true)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(NOTIFICATION_PERMANENT_ID, builder.build());
    }

    private void killContinuousNotification() {
        notificationManager.cancel(NOTIFICATION_PERMANENT_ID);
    }

    private void showWarningNotification(int percent) {
        Intent appIntent = new Intent(getApplicationContext(), SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_USAGE_WARNING_CHANNEL)
                .setSmallIcon(R.drawable.icon_warning)
                .setContentTitle(getString(R.string.notification_warning_title))
                .setContentText(String.format(getString(R.string.notification_warning_info), percent))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(NOTIFICATION_USAGE_WARNING_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
