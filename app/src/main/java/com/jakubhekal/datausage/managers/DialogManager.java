package com.jakubhekal.datausage.managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;
import com.jakubhekal.datausage.activities.SettingsActivity;
import com.jakubhekal.datausage.managers.PermissionManager;
import com.jakubhekal.datausage.managers.PreferenceManager;

import java.util.Objects;

public class DialogManager {

    /*public static void showHelpDialog(Context context, LayoutInflater inflater){

        final View dialogView = inflater.inflate(R.layout.dialog_help, null);

        AlertDialog dialogs = new MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .setCancelable(true)
                .show();

        final MaterialButton button = dialogView.findViewById(R.id.button_close);
        button.setOnClickListener(view -> dialogs.dismiss());

        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }*/

    public static void showThemeDialog(Context context, LayoutInflater inflater, PreferenceManager preferenceManager, Class<?> cls){

        final View dialogView = inflater.inflate(R.layout.dialog_theme, null);

        AlertDialog dialogs = new MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .setCancelable(true)
                .show();

        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final RadioButton radioLight = dialogView.findViewById(R.id.radio_limited);
        final RadioButton radioDark = dialogView.findViewById(R.id.radio_unlimited);
        final RadioButton radioSystem = dialogView.findViewById(R.id.radio_system);

        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                radioDark.setChecked(true);
                break;

            case AppCompatDelegate.MODE_NIGHT_NO:
                radioLight.setChecked(true);
                break;

            default:
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
            case AppCompatDelegate.MODE_NIGHT_UNSPECIFIED:
                radioSystem.setChecked(true);
                break;
        }


        radioLight.setOnCheckedChangeListener((compoundButton, b) -> {
            Utils.setTheme(context,preferenceManager,AppCompatDelegate.MODE_NIGHT_NO, cls);
            dialogs.dismiss();
        });

        radioDark.setOnCheckedChangeListener((compoundButton, b) -> {
            Utils.setTheme(context,preferenceManager,AppCompatDelegate.MODE_NIGHT_YES, cls);
            dialogs.dismiss();
        });


        radioSystem.setOnCheckedChangeListener((compoundButton, b) -> {
            Utils.setTheme(context,preferenceManager,AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, cls);
            dialogs.dismiss();
        });
    }

    public static void showPermissionsDialog(Context context, LayoutInflater inflater) {

        final View dialogView = inflater.inflate(R.layout.dialog_permissions, null);

        AlertDialog dialogs = new MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .setCancelable(false)
                .show();

        final MaterialButton button_yes = dialogView.findViewById(R.id.button_yes);
        button_yes.setOnClickListener(view -> {
            PermissionManager.requestPermissions(context);
            dialogs.dismiss();
        });

        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void showPeriodDialog(Context context, LayoutInflater inflater, PreferenceManager preferenceManager) {

        final View dialogView = inflater.inflate(R.layout.dialog_period, null);

        AlertDialog dialogs = new MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .setCancelable(true)
                .show();

        final EditText dayInput = dialogView.findViewById(R.id.dayInput);
        final MaterialButton button_yes = dialogView.findViewById(R.id.button_yes);

        dayInput.setText(String.valueOf(preferenceManager.getPeriodStart()));

        button_yes.setOnClickListener(view -> {
            if (!Objects.requireNonNull(dayInput.getText()).toString().trim().isEmpty()) {
                int day = Integer.parseInt(dayInput.getText().toString());
                if(day > 0 && day < 32) {
                    preferenceManager.setPeriodStart(day);
                }
            }
            ((SettingsActivity)context).initData();
            dialogs.dismiss();
        });


        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void showLimitsDialog(Context context, LayoutInflater inflater, PreferenceManager preferenceManager) {

        final View dialogView = inflater.inflate(R.layout.dialog_limits, null);

        AlertDialog dialogs = new MaterialAlertDialogBuilder(context)
                .setView(dialogView)
                .setCancelable(true)
                .show();

        final EditText periodInput = dialogView.findViewById(R.id.periodInput);
        final CheckBox periodUnlimited = dialogView.findViewById(R.id.periodUnlimited);
        final View periodInputContainer = dialogView.findViewById(R.id.periodInputContainer);
        final EditText dayInput = dialogView.findViewById(R.id.dayInput);
        final CheckBox dayCalculated = dialogView.findViewById(R.id.dayCalculated);
        final View dayInputContainer = dialogView.findViewById(R.id.dayInputContainer);
        final MaterialButton button_yes = dialogView.findViewById(R.id.button_yes);

        periodInput.setText(String.valueOf(preferenceManager.getPeriodLimit() / 1000000));
        periodInputContainer.setVisibility(preferenceManager.getPeriodLimitCustom() ? View.VISIBLE : View.GONE);
        periodUnlimited.setChecked(!preferenceManager.getPeriodLimitCustom());

        dayInput.setText(String.valueOf(preferenceManager.getDailyLimit() / 1000000));
        dayInputContainer.setVisibility(preferenceManager.getDailyLimitCustom() ? View.VISIBLE : View.GONE);
        dayCalculated.setChecked(!preferenceManager.getDailyLimitCustom());

        periodUnlimited.setOnClickListener(view -> periodInputContainer.setVisibility(((CheckBox)view).isChecked() ? View.GONE : View.VISIBLE));
        dayCalculated.setOnClickListener(view -> dayInputContainer.setVisibility(((CheckBox)view).isChecked() ? View.GONE : View.VISIBLE));

        button_yes.setOnClickListener(view -> {
            if (!Objects.requireNonNull(periodInput.getText()).toString().trim().isEmpty()) {
                preferenceManager.setPeriodLimit(Integer.parseInt(periodInput.getText().toString()) * 1000000L);
            }
            preferenceManager.setPeriodLimitCustom(!periodUnlimited.isChecked());
            if (!Objects.requireNonNull(dayInput.getText()).toString().trim().isEmpty()) {
                preferenceManager.setDailyLimit(Integer.parseInt(dayInput.getText().toString()) * 1000000L);
            }
            preferenceManager.setDailyLimitCustom(!dayCalculated.isChecked());
            ((SettingsActivity)context).initData();
            dialogs.dismiss();
        });


        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}