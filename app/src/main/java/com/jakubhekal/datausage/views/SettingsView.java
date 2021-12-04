package com.jakubhekal.datausage.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;

public class SettingsView extends RelativeLayout {

    Context context;
    AttributeSet attrs;
    int styleAttr;

    TextView textTitle, textInfo;
    ImageView imageIcon;
    SwitchMaterial switchState;

    public SettingsView(Context context) {
        super(context);
        this.context=context;
        initView();
    }
    public SettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        initView();
    }
    public SettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.attrs=attrs;
        this.styleAttr=defStyleAttr;
        initView();
    }

    private void initView() {

        inflate(context, R.layout.item_settings,this);
        TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.SettingsView,
                styleAttr,0);

        imageIcon = findViewById(R.id.app_icon);
        textTitle = findViewById(R.id.text_app_name);
        textInfo = findViewById(R.id.text_data_usage);
        switchState = findViewById(R.id.switch_state);
        setAttrsToView(arr);
        arr.recycle();
    }

    public void setAttrsToView(TypedArray arr){
        textTitle.setText(arr.getText(R.styleable.SettingsView_settingTitle));
        textInfo.setText(arr.getText(R.styleable.SettingsView_settingInfo));
        imageIcon.setImageDrawable(arr.getDrawable(R.styleable.SettingsView_settingIcon));
        imageIcon.setImageTintList(ColorStateList.valueOf(arr.getColor(R.styleable.SettingsView_settingIconTint, Utils.getAttrColor(context,R.attr.colorIcon))));
        switchState.setVisibility(arr.getInteger(R.styleable.SettingsView_settingSwitchVisibility, View.VISIBLE));
    }

    public void setIconTint(int color) {
        this.imageIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    public void setInfo(String info) {
        this.textInfo.setText(info);
    }

    public void setSwitchState(boolean state){
        switchState.setChecked(state);
    }

    public void setOnSwitchListener(CompoundButton.OnCheckedChangeListener listener){
        this.switchState.setOnCheckedChangeListener(listener);
    }

    public void performSwitchClick(){
        this.switchState.performClick();
    }
}