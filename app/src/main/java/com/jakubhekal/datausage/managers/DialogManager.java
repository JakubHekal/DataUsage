package com.jakubhekal.datausage.managers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;

import java.util.HashMap;
import java.util.Map;

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

    public static void showThemeDialog(Context context){

        View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_theme, null);

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.dialog_theme_title)
                .setView(dialogView)
                .setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dialog.dismiss())
                .show();

        RadioButton radioLight = dialogView.findViewById(R.id.radio_limited);
        RadioButton radioDark = dialogView.findViewById(R.id.radio_unlimited);
        RadioButton radioSystem = dialogView.findViewById(R.id.radio_system);

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
            Utils.setTheme(context, AppCompatDelegate.MODE_NIGHT_NO);
            alertDialog.dismiss();
        });

        radioDark.setOnCheckedChangeListener((compoundButton, b) -> {
            Utils.setTheme(context, AppCompatDelegate.MODE_NIGHT_YES);
            alertDialog.dismiss();
        });


        radioSystem.setOnCheckedChangeListener((compoundButton, b) -> {
            Utils.setTheme(context, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            alertDialog.dismiss();
        });
    }


    public static void showDataSizeDialog(Context context, int title, Long value, String unit, OnDataSizeDialogPositiveClick listener){

        View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_data_size, null);
        EditText inputData = dialogView.findViewById(R.id.input_data);
        MaterialButton unitPicker = dialogView.findViewById(R.id.input_unit);

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(dialogView)
                .setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> {
                    String currentUnit = unitPicker.getText().toString();
                    if(inputData.getText().length() > 0) {
                        listener.onClick(dialog, Long.valueOf(inputData.getText().toString()) * (currentUnit == DATA_SIZE_UNIT_MB ? DATA_SIZE_CONVERSION_MB : DATA_SIZE_CONVERSION_GB), currentUnit);
                    } else {
                        listener.onClick(dialog, 0L, unitPicker.getText().toString());
                    }

                })
                .show();

        inputData.setText(String.valueOf(value / (unit == DATA_SIZE_UNIT_MB ? DATA_SIZE_CONVERSION_MB : DATA_SIZE_CONVERSION_GB)));
        unitPicker.setText(unit);

        unitPicker.setOnClickListener(view -> {
            if(unitPicker.getText() == DATA_SIZE_UNIT_MB) {
                unitPicker.setText(DATA_SIZE_UNIT_GB);
            } else {
                unitPicker.setText(DATA_SIZE_UNIT_MB);
            }
        });
    }

    public static final String DATA_SIZE_UNIT_MB = "MB";
    public static final Long DATA_SIZE_CONVERSION_MB = 1000000L;
    public static final String DATA_SIZE_UNIT_GB = "GB";
    public static final Long DATA_SIZE_CONVERSION_GB = 1000000000L;

    public interface OnDataSizeDialogPositiveClick {
        void onClick(DialogInterface dialog, Long value, String unit);
    }

    public static void showDayPickerDialog(Context context, int title, int day, OnDayPickerPositiveClick listener){

        View dialogView = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_day_picker, null);

        NumberPicker dayPicker = dialogView.findViewById(R.id.number_picker_day);

        AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(dialogView)
                .setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> listener.onClick(dialog, dayPicker.getValue()))
                .show();

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(day);

    }

    public interface OnDayPickerPositiveClick {
        void onClick(DialogInterface dialog, int day);
    }

}
