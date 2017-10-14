package com.turlir.abakgists.templater.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.turlir.abakgists.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerticalEditText extends LinearLayout implements FormWidget {

    @BindView(R.id.templ_labeled_et_tv)
    TextView tv;

    @BindView(R.id.templ_labeled_et)
    EditText et;

    @BindView(R.id.templ_labeled_error)
    TextView error;

    public VerticalEditText(Context context) {
        super(context);

        LayoutInflater inf = LayoutInflater.from(context);
        View root = inf.inflate(R.layout.templ_vertical_edit_text, this, true);
        ButterKnife.bind(this, root);
    }

    public void setTitle(String title) {
        tv.setText(title);
    }

    public void setHint(String hint) {
        et.setHint(hint);
    }

    @Override
    public void bind(String origin) {
        et.setText(origin);
    }

    @Override
    public String content() {
        return et.getText().toString();
    }

    @Override
    public void showError(String msg) {
        error.setText(msg);
    }

    @Override
    public void position(@Position int position) {
        //
    }
}
