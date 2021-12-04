package com.jakubhekal.datausage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.views.SettingsView;

public class NotificationsActivity extends AppCompatActivity {

    SettingsView notificationPermanentView;
    SettingsView notificationWarningView;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.notifications_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_back);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED,null);
            finish();
        });

        notificationPermanentView = findViewById(R.id.notification_permanent);
        notificationWarningView = findViewById(R.id.notification_warning);

        notificationPermanentView.setOnSwitchListener((view, state) -> preferenceManager.setNotificationPermanent(state));
        notificationWarningView.setOnSwitchListener((view, state) -> preferenceManager.setNotificationWarning(state));

        initData();
    }

    public void initData() {
        preferenceManager.reload();
        notificationPermanentView.setSwitchState(preferenceManager.getNotificationPermanent());
        notificationWarningView.setSwitchState(preferenceManager.getNotificationWarning());
    }
}