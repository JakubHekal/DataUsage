package com.jakubhekal.datausage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.views.LineInputView;
import com.jakubhekal.datausage.views.LineSwitchView;

public class LimitsActivity extends AppCompatActivity {

    LineInputView inputPeriodStartView;
    LineInputView inputPeriodLimitView;
    LineInputView inputDailyLimitView;
    LineSwitchView switchDailyLimitCalculatedView;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limits);

        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.limit_title));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_back);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED,null);
            finish();
        });

        inputPeriodStartView = findViewById(R.id.period_start_day);
        inputPeriodLimitView = findViewById(R.id.period_limit_input);
        inputDailyLimitView = findViewById(R.id.daily_limit_input);
        switchDailyLimitCalculatedView = findViewById(R.id.daily_limit_calculated);

        inputPeriodStartView.addOnTextChangedListener(newText -> preferenceManager.setPeriodStart(Integer.parseInt(newText)));

        inputPeriodLimitView.addOnTextChangedListener(newText -> preferenceManager.setPeriodLimit(Integer.parseInt(newText) * 1000000L));

        inputDailyLimitView.addOnTextChangedListener(newText -> preferenceManager.setDailyLimit(Integer.parseInt(newText) * 1000000L));

        switchDailyLimitCalculatedView.addOnCheckedChangeListener((view, state) -> {
            inputDailyLimitView.setVisibility(state ? View.GONE : View.VISIBLE);
            preferenceManager.setDailyLimitCustom(!state);
        });

        initData();
    }

    public void initData() {
        preferenceManager.reload();

        inputPeriodStartView.setInputValue(String.valueOf(preferenceManager.getPeriodStart()));
        inputPeriodLimitView.setInputValue(String.valueOf(preferenceManager.getPeriodLimit() / 1000000L));
        inputDailyLimitView.setInputValue(String.valueOf(preferenceManager.getDailyLimit() / 1000000L));
        inputDailyLimitView.setVisibility(preferenceManager.getDailyLimitCustom() ? View.VISIBLE : View.GONE);
        switchDailyLimitCalculatedView.setSwitchState(!preferenceManager.getDailyLimitCustom());
    }
}