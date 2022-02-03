package com.jakubhekal.datausage;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class BottomNavigationPager {

    Context context;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Integer containerId;
    Map<Integer, Fragment> fragmentMap;
    Integer activeId;

    public BottomNavigationPager(Context context, BottomNavigationView bottomNavigationView, Integer containerId) {
        this.context = context;
        this.bottomNavigationView = bottomNavigationView;
        this.containerId = containerId;

        fragmentManager = ((Activity)context).getFragmentManager();
        for (Fragment fragment : fragmentManager.getFragments()) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }

        fragmentMap = new HashMap<>();
    }

    public void bindFragment(Integer id, Fragment fragmentInstance) {
        fragmentMap.put(id, fragmentInstance);
        fragmentManager.beginTransaction().add(containerId, fragmentInstance).commit();
        activeId = id;
    }

    public void enable() {
        refresh();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            changeFragment(item.getItemId());
            return true;
        });
    }

    public void refresh() {
        for(Map.Entry<Integer,Fragment> fragmentEntry : fragmentMap.entrySet()) {
            fragmentManager.beginTransaction().hide(fragmentEntry.getValue()).commit();
        }
        changeFragment(bottomNavigationView.getSelectedItemId());
    }

    private void changeFragment(Integer id) {
        fragmentManager.beginTransaction().hide(fragmentMap.get(activeId)).show(fragmentMap.get(id)).commit();
        activeId = id;
    }
}
