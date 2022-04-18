package com.jakubhekal.datausage;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class BottomNavigationPager {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Integer containerId;
    Map<Integer, Fragment> fragmentMap;
    Integer activeId;
    OnFragmentChangedListener listener;

    public BottomNavigationPager(Context context, BottomNavigationView bottomNavigationView, Integer containerId) {
        this.bottomNavigationView = bottomNavigationView;
        this.containerId = containerId;

        this.fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
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
        listener.onFragmentChanged(id, fragmentMap.get(activeId),fragmentMap.get(id));
        activeId = id;
    }

    public void setOnFragmentChangedListener(OnFragmentChangedListener listener) {
        this.listener = listener;
    }

    public interface OnFragmentChangedListener {
        void onFragmentChanged(Integer optionId, Fragment newFragment, Fragment oldFragment);
    }
}
