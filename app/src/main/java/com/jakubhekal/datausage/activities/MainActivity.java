package com.jakubhekal.datausage.activities;

import android.annotation.TargetApi;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jakubhekal.datausage.DateTimeUtils;
import com.jakubhekal.datausage.NotifyService;
import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.managers.NetworkUsageManager;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.managers.DialogManager;
import com.jakubhekal.datausage.managers.PermissionManager;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.views.LineView;
import com.jakubhekal.datausage.views.ProgressBarView;


public class MainActivity extends AppCompatActivity {

    ProgressBarView dailyUsageView;
    ProgressBarView periodUsageView;

    LineView periodView;
    LineView appsView;
    LineView settingsView;

    PreferenceManager preferenceManager;
    NetworkUsageManager networkUsageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(this);
        networkUsageManager = new NetworkUsageManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        dailyUsageView = findViewById(R.id.daily_usage);
        periodUsageView = findViewById(R.id.monthly_usage);

        periodView = findViewById(R.id.period);
        appsView = findViewById(R.id.apps);
        settingsView = findViewById(R.id.settings);

        appsView.setOnClickListener(view -> startActivity(new Intent(this, AppsActivity.class)));
        settingsView.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));

        if (PermissionManager.hasPermissions(this)) {
            calculateOverview();
            startService(new Intent(this, NotifyService.class));
        } else {
            DialogManager.showPermissionsDialog(this, getLayoutInflater());
        }
    }

    private void calculateOverview() {
        int daysTillEndOfPeriod = DateTimeUtils.getDaysTillPeriodEnd(preferenceManager);
        Long periodUsage = networkUsageManager.getAllBytesMobile(DateTimeUtils.getPeriodStartMillis(preferenceManager.getPeriodStart()), DateTimeUtils.getDayEndMillis());
        Long periodLimit = preferenceManager.getPeriodLimit();

        periodUsageView.setData(Utils.convertFromBytes(periodUsage), Utils.convertFromBytes(periodLimit), ((float)periodUsage / periodLimit));
        periodView.setInfo(Utils.formatDaysReaming(this, daysTillEndOfPeriod, DateTimeUtils.dateFromMillis(DateTimeUtils.getPeriodEndMillis(preferenceManager.getPeriodStart()))));

        Long dailyUsage = networkUsageManager.getAllBytesMobile(DateTimeUtils.getDayStartMillis(), DateTimeUtils.getDayEndMillis());
        Long dailyLimit = preferenceManager.getDailyLimitCustom() ? preferenceManager.getDailyLimit() : (periodLimit - (periodUsage-dailyUsage)) / daysTillEndOfPeriod;

        dailyUsageView.setData(Utils.convertFromBytes(dailyUsage), Utils.convertFromBytes(dailyLimit), ((float)dailyUsage / dailyLimit));
    }


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();
        if (PermissionManager.hasPermissions(this)) {
            calculateOverview();
            startService(new Intent(this, NotifyService.class));
        } else {
            DialogManager.showPermissionsDialog(this, getLayoutInflater());
        }
    }
}
