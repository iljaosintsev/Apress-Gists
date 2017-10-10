package com.turlir.abakgists.templater.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.turlir.abakgists.R;

import butterknife.BindDimen;
import butterknife.ButterKnife;

public class MaterialField extends TextInputLayout implements FormWidget<String> {

    @BindDimen(R.dimen.four_margin)
    int four;

    public MaterialField(Context context) {
        super(context);
        ButterKnife.bind(this);

        setHintEnabled(true);
        setPadding(0, four, 0, four);
        setErrorEnabled(true);

        TextInputEditText et = new TextInputEditText(context);
        et.setMaxLines(1);
        et.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        addView(et);
    }

    @Override
    public void bind(String origin) {
        if (getEditText() != null) {
            boolean tmp = isHintAnimationEnabled();
            setHintAnimationEnabled(false);
            getEditText().setText(origin);
            setHintEnabled(tmp);
        }
    }

    @Override
    public String content() {
        if (getEditText() != null) {
            return getEditText().getText().toString();
        }
        return null;
    }

    @Override
    public void showError(String error) {
        setError(error);
    }

    @Override
    public void position(@Position int position) {
        if (getEditText() == null) return;
        switch (position) {
            case FIRST:
            case MIDDLE:
                getEditText().setImeOptions(EditorInfo.IME_ACTION_NEXT);
                break;
            case LAST:
                getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
                break;
        }
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        if (getEditText() != null) {
            getEditText().setOnEditorActionListener(listener);
        }
    }
}
