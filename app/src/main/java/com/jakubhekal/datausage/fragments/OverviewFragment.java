package com.jakubhekal.datausage.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.jakubhekal.datausage.DateTimeUtils;
import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.managers.NetworkUsageManager;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.views.UsageBarView;


public class OverviewFragment extends Fragment {

    Context context;

    UsageBarView dailyUsageBarView;
    UsageBarView periodUsageBarView;
    TextView currentPeriodView;

    PreferenceManager preferenceManager;
    NetworkUsageManager networkUsageManager;

    public OverviewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        context = getContext();
        dailyUsageBarView = root.findViewById(R.id.daily_usage_bar);
        periodUsageBarView = root.findViewById(R.id.period_usage_bar);
        currentPeriodView = root.findViewById(R.id.current_period_value);

        preferenceManager = new PreferenceManager(context);
        networkUsageManager = new NetworkUsageManager(context);

        calculateOverview();

        return root;
    }

    private void calculateOverview() {
        int daysTillEndOfPeriod = DateTimeUtils.getDaysTillPeriodEnd(preferenceManager);
        Long periodUsage = networkUsageManager.getAllBytesMobile(DateTimeUtils.getPeriodStartMillis(preferenceManager.getPeriodStart()), DateTimeUtils.getDayEndMillis());
        Long periodLimit = preferenceManager.getPeriodLimit();

        periodUsageBarView.setData(periodUsage, periodLimit);

        Long dailyUsage = networkUsageManager.getAllBytesMobile(DateTimeUtils.getDayStartMillis(), DateTimeUtils.getDayEndMillis());
        Long dailyLimit = preferenceManager.getDailyLimitCustom() ? preferenceManager.getDailyLimit() : (periodLimit - (periodUsage-dailyUsage)) / daysTillEndOfPeriod;

        dailyUsageBarView.setData(dailyUsage, dailyLimit);

        currentPeriodView.setText(DateTimeUtils.dateFromMillis(DateTimeUtils.getPeriodStartMillis(preferenceManager.getPeriodStart())) + " - " + DateTimeUtils.dateFromMillis(DateTimeUtils.getPeriodEndMillis(preferenceManager.getPeriodStart())));
    }
}