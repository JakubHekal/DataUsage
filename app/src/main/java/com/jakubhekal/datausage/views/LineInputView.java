package com.jakubhekal.datausage.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakubhekal.datausage.R;
import com.jakubhekal.datausage.Utils;

public class LineInputView extends RelativeLayout {

    Context context;
    AttributeSet attrs;
    int styleAttr;

    OnTextChangedListener listener;
    String defaultValue;

    ImageView imageIcon;
    TextView textTitle;
    TextView textLabel;
    EditText inputEdit;

    public LineInputView(Context context) {
        super(context);
        this.context = context;
        initView();
    }
    public LineInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }
    public LineInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        inflate(context, R.layout.item_line_input,this);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.LineView, styleAttr,0);

        imageIcon = findViewById(R.id.line_input_icon);
        textTitle = findViewById(R.id.line_input_title);
        textLabel = findViewById(R.id.line_input_label);
        inputEdit = findViewById(R.id.line_input_input);

        imageIcon.setImageDrawable(arr.getDrawable(R.styleable.LineView_imageIcon));
        imageIcon.setImageTintList(ColorStateList.valueOf(arr.getColor(R.styleable.LineView_tintIcon, Utils.getAttrColor(context,R.attr.colorIcon))));
        textTitle.setText(arr.getText(R.styleable.LineView_textTitle));
        textLabel.setText(arr.getText(R.styleable.LineView_textLabel));
        inputEdit.setInputType(arr.getInteger(R.styleable.LineView_android_inputType, InputType.TYPE_CLASS_TEXT));
        inputEdit.setHint(arr.getText(R.styleable.LineView_textHint));

        defaultValue = arr.getString(R.styleable.LineView_textDefaultValue);

        arr.recycle();
    }

    public void setInputValue(String value) {
        inputEdit.setText(value);
    }

    public void setInputSelection(int index) { inputEdit.setSelection(index); }

    public void addOnTextChangedListener(OnTextChangedListener listener) {
        this.listener = listener;
        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    listener.onTextChanged(charSequence.toString());
                } else {
                    listener.onTextChanged(defaultValue);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public interface OnTextChangedListener {
        void onTextChanged(String newText);
    }
}