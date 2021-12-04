package com.jakubhekal.datausage.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;

public class ProgressBarView extends RelativeLayout {

    Context context;
    AttributeSet attrs;
    int styleAttr;

    TextView textTitle, textValue, textLimit;
    ProgressBar progressBar;

    public ProgressBarView(Context context) {
        super(context);
        this.context=context;
        initView();
    }
    public ProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        initView();
    }
    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.attrs=attrs;
        this.styleAttr=defStyleAttr;
        initView();
    }

    private void initView() {

        inflate(context, R.layout.item_progress_bar,this);
        TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.ProgressBarView, styleAttr,0);

        textTitle = findViewById(R.id.bar_title);
        textValue = findViewById(R.id.bar_value);
        textLimit = findViewById(R.id.bar_limit);

        progressBar = findViewById(R.id.progress_bar);

        setAttrsToView(arr);
        arr.recycle();
    }

    public void setAttrsToView(TypedArray arr){
        textTitle.setText(arr.getText(R.styleable.ProgressBarView_textBigTitle));
        textValue.setText(arr.getText(R.styleable.ProgressBarView_textValue));
        textLimit.setText(arr.getText(R.styleable.ProgressBarView_textLimit));
    }

    public void setData(String value, String limit, float percentage){
        Log.d("S", String.valueOf(Math.round(percentage * 1000)));
        progressBar.setProgress(Math.round(percentage * 1000));
        textValue.setText(value);
        textLimit.setText(limit);
        if(percentage > 0.9f) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Utils.getAttrColor(context, R.attr.colorHigh)));
            textValue.setTextColor(Utils.getAttrColor(context, R.attr.colorHigh));
        }
        else if(percentage > 0.75f) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Utils.getAttrColor(context, R.attr.colorMedium)));
            textValue.setTextColor(Utils.getAttrColor(context, R.attr.colorMedium));
        }
        else {
            progressBar.setProgressTintList(ColorStateList.valueOf(Utils.getAttrColor(context, R.attr.colorLow)));
            textValue.setTextColor(Utils.getAttrColor(context, R.attr.colorLow));
        }
    }

}