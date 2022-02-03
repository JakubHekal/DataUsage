package com.jakubhekal.datausage.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;

public class UsageBarView extends RelativeLayout {

    Context context;
    AttributeSet attrs;
    int styleAttr;

    TextView textPercentage, textPercentageLabel, textUsed, textLeft;
    ProgressBar progressBar;

    public UsageBarView(Context context) {
        super(context);
        this.context = context;
        initView();
    }
    public UsageBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }
    public UsageBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {

        inflate(context, R.layout.item_usage_bar,this);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.UsageBarView, styleAttr,0);

        textPercentage = findViewById(R.id.usage_bar_percentage);
        textPercentageLabel = findViewById(R.id.usage_bar_percentage_label);
        textUsed = findViewById(R.id.usage_bar_used);
        textLeft = findViewById(R.id.usage_bar_left);

        progressBar = findViewById(R.id.usage_bar_bar);

        setAttrsToView(arr);
        arr.recycle();
    }

    public void setAttrsToView(TypedArray arr){
        textPercentageLabel.setText(arr.getText(R.styleable.UsageBarView_textPercentageLabel));
    }

    public void setData(long used, long limit){
        int percentage = Math.round(((float)used / limit) * 100);
        progressBar.setProgress(percentage);
        textPercentage.setText(percentage + "%");
        textUsed.setText(Utils.convertFromBytes(used));
        textLeft.setText(Utils.convertFromBytes(limit - used));
    }
}
