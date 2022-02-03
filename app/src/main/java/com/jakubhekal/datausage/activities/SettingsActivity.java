package com.jakubhekal.datausage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.managers.PermissionManager;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.managers.DialogManager;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.views.LineSwitchView;
import com.jakubhekal.datausage.views.LineView;

public class SettingsActivity extends AppCompatActivity {

    LineView settingThemeView;
    LineView settingPeriodStartDayView;
    LineView settingPeriodLimitView;
    LineSwitchView settingDailyLimitCalculatedView;
    LineView settingDailyLimitView;
    LineSwitchView settingNotificationPermanent;
    LineSwitchView settingPermissionPhone;
    LineSwitchView settingPermissionUsageAccess;
    LineSwitchView settingPermissionIgnoreBatteryOptimization;
    LineView settingInformationGithub;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED,null);
            finish();
        });

        settingThemeView = findViewById(R.id.setting_theme);
        settingPeriodStartDayView = findViewById(R.id.setting_period_start_day);
        settingPeriodLimitView = findViewById(R.id.setting_period_limit);
        settingDailyLimitCalculatedView = findViewById(R.id.setting_daily_limit_calculated);
        settingDailyLimitView = findViewById(R.id.setting_daily_limit);
        settingNotificationPermanent = findViewById(R.id.setting_notification_permanent);
        settingPermissionPhone = findViewById(R.id.setting_permission_phone);
        settingPermissionUsageAccess = findViewById(R.id.setting_permission_usage_access);
        settingPermissionIgnoreBatteryOptimization = findViewById(R.id.setting_permission_ignore_battery_optimization);
        settingInformationGithub = findViewById(R.id.setting_information_github);

        settingThemeView.setOnClickListener(view -> DialogManager.showThemeDialog(this));
        settingPeriodStartDayView.setOnClickListener(view ->
                DialogManager.showDayPickerDialog(this, R.string.setting_period_starting_day_title, preferenceManager.getPeriodStart(), (dialog, value) -> {
                    preferenceManager.setPeriodStart(value);
                    initData();
                })
        );
        settingPeriodLimitView.setOnClickListener(view ->
            DialogManager.showDataSizeDialog(this, R.string.setting_period_limit_title, preferenceManager.getPeriodLimit(), preferenceManager.getPeriodLimitUnit(), (dialog, value, unit) -> {
                preferenceManager.setPeriodLimit(value);
                preferenceManager.setPeriodLimitUnit(unit);
                initData();
            })
        );
        settingDailyLimitCalculatedView.addOnCheckedChangeListener((view, isChecked) -> {
            preferenceManager.setDailyLimitCustom(!isChecked);
            initData();
        });
        settingDailyLimitView.setOnClickListener(view ->
                DialogManager.showDataSizeDialog(this, R.string.setting_daily_limit_title, preferenceManager.getDailyLimit(), preferenceManager.getDailyLimitUnit(), (dialog, value, unit) -> {
                    preferenceManager.setDailyLimit(value);
                    preferenceManager.setDailyLimitUnit(unit);
                    initData();
                })
        );
        settingNotificationPermanent.addOnCheckedChangeListener((view, isChecked) -> {
            preferenceManager.setNotificationPermanent(isChecked);
            initData();
        });

        settingPermissionPhone.addOnCheckedChangeListener((view, isChecked) -> {
            if(isChecked) {
                PermissionManager.requestPermissionToReadPhoneStats(this);
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:"+ getPackageName()));
                startActivity(intent);
            }
            initData();
        });

        settingPermissionUsageAccess.addOnCheckedChangeListener((view, isChecked) -> {
            PermissionManager.requestPermissionToReadNetworkHistory(this);
            initData();
        });

        settingPermissionIgnoreBatteryOptimization.addOnCheckedChangeListener((view, isChecked) -> {
            if(isChecked) {
                PermissionManager.requestPermissionToIgnoreBatteryOptimizations(this);
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivity(intent);
            }
            initData();
        });

        settingInformationGithub.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JakubHekal/DataUsage"))));

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData() {
        preferenceManager.reload();
        settingThemeView.setInfo(Utils.getThemeName(this));
        settingPeriodLimitView.setInfo(Utils.convertFromBytes(preferenceManager.getPeriodLimit()));
        settingPeriodStartDayView.setInfo(String.valueOf(preferenceManager.getPeriodStart()));
        settingDailyLimitCalculatedView.setSwitchState(!preferenceManager.getDailyLimitCustom());
        settingDailyLimitView.setVisibility(preferenceManager.getDailyLimitCustom() ? View.VISIBLE : View.GONE);
        settingDailyLimitView.setInfo(Utils.convertFromBytes(preferenceManager.getDailyLimit()));
        settingNotificationPermanent.setSwitchState(preferenceManager.getNotificationPermanent());
        settingPermissionPhone.setSwitchState(PermissionManager.hasPermissionToReadPhoneStats(this));
        settingPermissionUsageAccess.setSwitchState(PermissionManager.hasPermissionToReadNetworkHistory(this));
        settingPermissionIgnoreBatteryOptimization.setSwitchState(PermissionManager.hasPermissionToIgnoreBatteryOptimizations(this));
    }

}