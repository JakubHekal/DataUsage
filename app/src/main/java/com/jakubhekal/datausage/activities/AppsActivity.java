package com.jakubhekal.datausage.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.jakubhekal.datausage.DateTimeUtils;
import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.managers.NetworkUsageManager;
import com.jakubhekal.datausage.managers.PreferenceManager;
import com.jakubhekal.datausage.model.Package;
import com.jakubhekal.datausage.PackageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    RecyclerView recyclerView;
    View loadingView;
    View emptyView;

    PreferenceManager preferenceManager;
    NetworkUsageManager networkUsageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getCurrentTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        preferenceManager = new PreferenceManager(this);
        networkUsageManager = new NetworkUsageManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.activity_title_apps));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_back);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED,null);
            finish();
        });

        tabLayout = findViewById(R.id.tabs);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadingView = findViewById(R.id.loading);
        emptyView = findViewById(R.id.empty);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                initData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                initData();
            }
        });

        initData();

    }

    private void initData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        executor.execute(() -> {
            List<Package> packageList = getPackages();
            handler.post(() -> {
                recyclerView.setAdapter(new PackageAdapter(packageList));
                loadingView.setVisibility(View.GONE);
                if(packageList.size() <= 0) {
                    emptyView.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    private List<Package> getPackages() {
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);

        List<Package> packageList = new ArrayList<>(packageInfoList.size());
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageManager.checkPermission(Manifest.permission.INTERNET, packageInfo.packageName) == PackageManager.PERMISSION_DENIED) {
                continue;
            }

            Long dataUsage = 0L;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(tabLayout.getSelectedTabPosition() == 0) {
                    dataUsage = networkUsageManager.getPackageBytesMobile(packageInfo.applicationInfo.uid, DateTimeUtils.getDayStartMillis(), DateTimeUtils.getDayEndMillis());
                } else {
                    dataUsage = networkUsageManager.getPackageBytesMobile(packageInfo.applicationInfo.uid, DateTimeUtils.getPeriodStartMillis(preferenceManager.getPeriodStart()), DateTimeUtils.getPeriodEndMillis(preferenceManager.getPeriodStart()));
                }
            }

            if(dataUsage <= 0) {
                continue;
            }

            Package packageItem = new Package();
            packageItem.setPackageName(packageInfo.packageName);
            packageItem.setDataUsage(dataUsage);
            packageList.add(packageItem);
            ApplicationInfo ai = null;
            try {
                ai = packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (ai == null) {
                continue;
            }
            CharSequence appName = packageManager.getApplicationLabel(ai);
            if (appName != null) {
                packageItem.setName(appName.toString());
            }
        }

        Collections.sort(packageList, new Comparator<Package>() {
            @Override
            public int compare(Package p1, Package p2) {
                return (int) ((p2.getDataUsage() - p1.getDataUsage()) / 10);
            }
        });

        return packageList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh){
            initData();
        }

        return super.onOptionsItemSelected(item);
    }
}