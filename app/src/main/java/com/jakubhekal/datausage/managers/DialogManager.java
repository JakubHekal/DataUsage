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

}