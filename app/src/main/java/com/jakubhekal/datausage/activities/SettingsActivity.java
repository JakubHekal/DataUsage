package com.jakubhekal.datausage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.managers.DialogManager;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.views.LineSwitchView;
import com.jakubhekal.datausage.views.LineView;

public class SettingsActivity extends AppCompatActivity {

    LineView settingThemeView;
    LineView settingLimitView;
    LineView settingNotificationsView;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.activity_title_settings));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_back);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED,null);
            finish();
        });

        settingThemeView = findViewById(R.id.setting_theme);
        settingLimitView = findViewById(R.id.setting_limit);
        settingNotificationsView = findViewById(R.id.setting_notifications);

        settingThemeView.setOnClickListener(view -> DialogManager.showThemeDialog(this, getLayoutInflater(),preferenceManager, SettingsActivity.class));
        settingLimitView.setOnClickListener(view -> startActivity(new Intent(this, LimitsActivity.class)));
        settingNotificationsView.setOnClickListener(view -> startActivity(new Intent(this, NotificationsActivity.class)));

        initData();
    }

    public void initData() {
        preferenceManager.reload();
        settingThemeView.setInfo(Utils.getThemeName(this));
    }

}