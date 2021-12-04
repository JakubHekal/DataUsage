package com.jakubhekal.datausage.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;

public class LineView extends RelativeLayout {

    Context context;
    AttributeSet attrs;
    int styleAttr;

    TextView textTitle, textInfo;
    ImageView imageIcon;

    public LineView(Context context) {
        super(context);
        this.context=context;
        initView();
    }
    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        this.attrs=attrs;
        initView();
    }
    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        this.attrs=attrs;
        this.styleAttr=defStyleAttr;
        initView();
    }

    private void initView() {

        inflate(context, R.layout.item_main,this);
        TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.LineView,
                styleAttr,0);

        imageIcon = findViewById(R.id.app_icon);
        textTitle = findViewById(R.id.text_app_name);
        textInfo = findViewById(R.id.text_data_usage);

        setAttrsToView(arr);
        arr.recycle();
    }

    public void setAttrsToView(TypedArray arr){
        textTitle.setText(arr.getText(R.styleable.LineView_textTitle));
        textInfo.setText(arr.getText(R.styleable.LineView_textInfo));
        imageIcon.setImageDrawable(arr.getDrawable(R.styleable.LineView_imageIcon));
        imageIcon.setImageTintList(ColorStateList.valueOf(arr.getColor(R.styleable.LineView_tintColor, Utils.getAttrColor(context,R.attr.colorIcon))));
    }

    public void setInfo(String info){
        textInfo.setText(info);
    }

}