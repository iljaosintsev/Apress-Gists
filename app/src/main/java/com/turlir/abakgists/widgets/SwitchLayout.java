package com.turlir.abakgists.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.turlir.abakgists.R;
import com.turlir.tokenizelayout.TokenizeLayout;

public class SwitchLayout extends TokenizeLayout {

    public static final int
            CONTENT = 0,
            ERROR = 1,
            LOADING = 1 << 1;

    public SwitchLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getDefaultToken(final AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchLayout, 0, 0);
        try {
            return ta.getInteger(R.styleable.SwitchLayout_init, 0);
        } finally {
            ta.recycle();
        }
    }

    @Override
    public int getMaxTokenCount() {
        return 3;
    }

    @Override
    public boolean doesViewToToken(int token, int index) {
        return token == index;
    }

    @Override
    public int getChildIndexByToken(int token) {
        if (getChildCount() > token) {
            return token;
        } else {
            return INVALID_INDEX;
        }
    }

    public void toContent() {
        changeToken(CONTENT);
    }

    public void toError() {
        changeToken(ERROR);
    }

    public void toLoading() {
        changeToken(LOADING);
    }

}
