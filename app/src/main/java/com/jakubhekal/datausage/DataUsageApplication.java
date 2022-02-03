package com.jakubhekal.datausage;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class DataUsageApplication extends Application {
    @Override
    public void onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(this);
        super.onCreate();
    }
}
