package com.smontiel.ferretera.admin.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.smontiel.ferretera.admin.R;

import java.util.regex.Pattern;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class CustomEditText extends TextInputLayout {
    private CharSequence error;
    private TextInputEditText editText;
    private Pattern pattern;
    private boolean canStartWithWhitespace;
    private boolean isValid;

    public CustomEditText(Context context) {
        super(context);
        init(context, null);
    }

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
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();

                if (pattern != null) {
                    isValid = pattern.matcher(text).matches();
                    if (isValid) {
                        if (error != null) CustomEditText.super.setError(null);
                    } else {
                        if (error != null) CustomEditText.super.setError(error);
                    }
                } else isValid = true;
            }

            @Override public void afterTextChanged(Editable editable) {
                if (!canStartWithWhitespace && editable.toString().startsWith(" ")) {
                    editText.setText(editable.toString().trim());
                }
            }
        });

        a.recycle();
    }

    public void canStartWithWhitespace(boolean value) {
        this.canStartWithWhitespace = value;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setErrorLabel(CharSequence error) {
        this.error = error;
    }

    public void showError(CharSequence error) {
        super.setError(error);
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void validateWith(Pattern pattern) {
        this.pattern = pattern;
    }
}
