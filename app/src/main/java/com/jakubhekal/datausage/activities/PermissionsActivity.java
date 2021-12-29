package com.jakubhekal.datausage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.managers.PermissionManager;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.views.LineSwitchView;

public class PermissionsActivity extends AppCompatActivity {

    LineSwitchView permissionPhoneView;
    LineSwitchView permissionIgnoreBatteryOptimizationView;
    LineSwitchView permissionUsageAccessView;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.activity_title_permissions));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_back);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED,null);
            finish();
        });

        permissionPhoneView = findViewById(R.id.permission_phone);
        permissionIgnoreBatteryOptimizationView = findViewById(R.id.permission_ignore_battery_optimization);
        permissionUsageAccessView = findViewById(R.id.permission_usage_access);

        permissionPhoneView.addOnCheckedChangeListener((view, state) -> {
            if(state) {
                PermissionManager.requestPermissionToReadPhoneStats(this);
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:"+ getPackageName()));
                startActivity(intent);
            }
            initData();
        });

        permissionIgnoreBatteryOptimizationView.addOnCheckedChangeListener((view, state) -> {
            if(state) {
                PermissionManager.requestPermissionToIgnoreBatteryOptimizations(this);
            } else {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                startActivity(intent);
            }
            initData();
        });

        permissionUsageAccessView.addOnCheckedChangeListener((view, state) -> {
            PermissionManager.requestPermissionToReadNetworkHistory(this);
            initData();
        });

        initData();
    }

    public void initData() {
        preferenceManager.reload();
        permissionPhoneView.setSwitchState(PermissionManager.hasPermissionToReadPhoneStats(this));
        permissionIgnoreBatteryOptimizationView.setSwitchState(PermissionManager.hasPermissionToIgnoreBatteryOptimizations(this));
        permissionUsageAccessView.setSwitchState(PermissionManager.hasPermissionToReadNetworkHistory(this));
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }
}