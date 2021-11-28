package com.jakubhekal.datausage;

import android.annotation.TargetApi;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jakubhekal.datausage.utils.PreferenceManager;
import com.jakubhekal.datausage.utils.Dialogs;
import com.jakubhekal.datausage.utils.NetworkStatsHelper;
import com.jakubhekal.datausage.utils.Permissions;
import com.jakubhekal.datausage.utils.Utils;
import com.jakubhekal.datausage.views.LineView;
import com.jakubhekal.datausage.views.ProgressBarView;


public class MainActivity extends AppCompatActivity {

    ProgressBarView dailyUsageView;
    ProgressBarView periodUsageView;

    LineView periodView;
    LineView appsView;
    LineView settingsView;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        dailyUsageView = findViewById(R.id.daily_usage);
        periodUsageView = findViewById(R.id.monthly_usage);

        periodView = findViewById(R.id.period);
        appsView = findViewById(R.id.apps);
        settingsView = findViewById(R.id.settings);

        periodView.setOnClickListener(view -> {});
        appsView.setOnClickListener(view -> startActivity(new Intent(this, AppsActivity.class)));
        settingsView.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));

        if (Permissions.hasPermissions(this)) {
            calculateOverview();
            startService(new Intent(this, NotifyService.class));
        } else {
            Dialogs.showPermissionsDialog(this, getLayoutInflater());
        }
    }

    private void calculateOverview() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager);

            int daysTillEndOfPeriod = (int) ((Utils.getPeriodEndMillis(preferenceManager.getPeriodStart()) - Utils.getDayStartMillis()) / (1000*60*60*24)) + 1;
            Long periodUsage = networkStatsHelper.getAllBytesMobile(this, Utils.getPeriodStartMillis(preferenceManager.getPeriodStart()));
            Long periodLimit = preferenceManager.getPeriodLimit();

            periodUsageView.setData(Utils.convertFromBytes(periodUsage), Utils.convertFromBytes(periodLimit), ((float)periodUsage / periodLimit));
            periodView.setInfo(Utils.formatDaysReaming(this, daysTillEndOfPeriod, Utils.dateFromMillis(Utils.getPeriodEndMillis(preferenceManager.getPeriodStart()))));

            Long dailyUsage = networkStatsHelper.getAllBytesMobile(this, Utils.getDayStartMillis());
            Long dailyLimit = preferenceManager.getDailyLimitCustom() ? preferenceManager.getDailyLimit() : (periodLimit - (periodUsage-dailyUsage)) / daysTillEndOfPeriod;

            dailyUsageView.setData(Utils.convertFromBytes(dailyUsage), Utils.convertFromBytes(dailyLimit), ((float)dailyUsage / dailyLimit));
        }
    }


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();
        if (Permissions.hasPermissions(this)) {
            calculateOverview();
            startService(new Intent(this, NotifyService.class));
        } else {
            Dialogs.showPermissionsDialog(this, getLayoutInflater());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh){
            calculateOverview();
            startService(new Intent(this, NotifyService.class));
            Toast.makeText(this,getString(R.string.refreshed), Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
