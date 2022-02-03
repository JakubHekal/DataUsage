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

    ImageView imageIcon;
    TextView textTitle;
    TextView textInfo;

    public LineView(Context context) {
        super(context);
        this.context = context;
        initView();
    }
    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }
    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        inflate(context, R.layout.item_line,this);
        TypedArray arr = context.obtainStyledAttributes(attrs,R.styleable.LineView, styleAttr,0);

        //imageIcon = findViewById(R.id.line_icon);
        textTitle = findViewById(R.id.line_title);
        textInfo = findViewById(R.id.line_info);

        //imageIcon.setImageDrawable(arr.getDrawable(R.styleable.LineView_imageIcon));
        //imageIcon.setImageTintList(ColorStateList.valueOf(arr.getColor(R.styleable.LineView_tintIcon, Utils.getAttrColor(context,R.attr.colorIcon))));
        textTitle.setText(arr.getText(R.styleable.LineView_textTitle));
        setInfo(arr.getText(R.styleable.LineView_textInfo));

        arr.recycle();
    }

    public void setInfo(CharSequence info) {
        if(info == null) {
            setInfo(null);
        } else {
            setInfo(info.toString());
        }
    }

    public void setInfo(String info){
        if(info == null) {
            textInfo.setVisibility(GONE);
        } else {
            textInfo.setVisibility(VISIBLE);
            textInfo.setText(info);
        }
    }

}