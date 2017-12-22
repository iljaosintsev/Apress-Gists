package com.turlir.abakgists.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.view.CollapsibleActionView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.turlir.abakgists.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Виджет для тулбара. Содержит EditText и иконку крестик
 */
public class SearchingView extends LinearLayout implements CollapsibleActionView {

    @BindView(R.id.autocomplete)
    EditText content;

    public SearchingView(Context context) {
        this(context, null);
    }

    public SearchingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inf = LayoutInflater.from(context);
        View root = inf.inflate(R.layout.searching_view, this, true);

        ButterKnife.bind(this, root);
    }

    @Override
    public void onActionViewExpanded() {
        getLayoutParams().width = LayoutParams.MATCH_PARENT;
        showKeyboard(content);
    }

    @Override
    public void onActionViewCollapsed() {
        hideKeyboard(content);
    }

    @OnClick(R.id.right_icon)
    void leftClick() {
        content.setText("");
    }

    public EditText editText() {
        return content;
    }

    private static void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)
                v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
