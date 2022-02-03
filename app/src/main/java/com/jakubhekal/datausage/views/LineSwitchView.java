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

public class LineSwitchView extends RelativeLayout {

    Context context;
    AttributeSet attrs;
    int styleAttr;

    ImageView imageIcon;
    TextView textTitle;
    TextView textInfo;
    SwitchMaterial inputSwitch;

    public LineSwitchView(Context context) {
        super(context);
        this.context = context;
        initView();
    }
    public LineSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }
    public LineSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        inflate(context, R.layout.item_line_switch,this);
        TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.LineView, styleAttr,0);

        //imageIcon = findViewById(R.id.line_switch_icon);
        textTitle = findViewById(R.id.line_switch_title);
        textInfo = findViewById(R.id.line_switch_info);
        inputSwitch = findViewById(R.id.line_switch_switch);

        //imageIcon.setImageDrawable(arr.getDrawable(R.styleable.LineView_imageIcon));
        //imageIcon.setImageTintList(ColorStateList.valueOf(arr.getColor(R.styleable.LineView_tintIcon, Utils.getAttrColor(context,R.attr.colorIcon))));
        textTitle.setText(arr.getText(R.styleable.LineView_textTitle));
        if(arr.getText(R.styleable.LineView_textInfo) == null) {
            textInfo.setVisibility(GONE);
        } else {
            textInfo.setText(arr.getText(R.styleable.LineView_textInfo));
        }

        arr.recycle();
    }

    public void setInfo(String info) {
        textInfo.setText(info);
    }

    public void setSwitchState(boolean state){
        inputSwitch.setChecked(state);
    }

    public void addOnCheckedChangeListener(SwitchMaterial.OnCheckedChangeListener listener) {
        inputSwitch.setOnClickListener(view -> {
            SwitchMaterial switchMaterial = (SwitchMaterial) view;
            listener.onCheckedChanged(switchMaterial, switchMaterial.isChecked());
        });
    }
}