package com.smontiel.ferretera.admin.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

import com.smontiel.ferretera.admin.R;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class CustomEditText extends TextInputLayout {
    private TextInputEditText editText;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.custom_edit_text, this);
        editText = findViewById(R.id._custom_edit_text);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0, 0);
        int inputType = a.getInt(R.styleable.CustomEditText_android_inputType, -1);
        if (inputType != -1) editText.setInputType(inputType);
        editText.callOnClick();

        a.recycle();
    }

    public String getText() {
        return editText.getText().toString();
    }

    public TextInputEditText getEditText() {
        return editText;
    }
}
