package example.turlir.com.templater.widget;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import example.turlir.com.templater.R;

public class MaterialField extends TextInputLayout implements FormWidget {

    int four;

    public MaterialField(Context context) {
        super(context);

        four = context.getResources().getDimensionPixelSize(R.dimen.four_margin);

        setHintEnabled(true);
        setPadding(0, four, 0, four);
        setErrorEnabled(true);

        TextInputEditText et = new TextInputEditText(context);
        et.setMaxLines(1);
        et.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        addView(et);
    }

    @Override
    public View view() {
        return this;
    }

    @Override
    public void bind(String origin) {
        if (getEditText() != null) {
            boolean tmp = isHintAnimationEnabled();
            setHintAnimationEnabled(false);
            getEditText().setText(origin);
            setHintAnimationEnabled(tmp);
        }
    }

    @Override
    public String content() {
        if (getEditText() != null) {
            return getEditText().getText().toString();
        }
        return "";
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

    @Override
    public void setName(String name) {
        super.setHint(name);
    }

    @Override
    public void setHint(String hint) {
        //
    }

    @Override
    public void setExample(String example) {
        // stub
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        if (getEditText() != null) {
            getEditText().setOnEditorActionListener(listener);
        }
    }
}
