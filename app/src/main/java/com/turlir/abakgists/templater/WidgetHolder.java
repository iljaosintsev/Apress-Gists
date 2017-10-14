package com.turlir.abakgists.templater;

import android.view.View;
import android.view.ViewGroup;

import com.turlir.abakgists.templater.base.EmptyHandler;
import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.check.Checker;
import com.turlir.abakgists.templater.widget.FormWidget;

public class WidgetHolder<T extends View & FormWidget> {

    private final T mWidget;
    private final Checker mChecker;
    private final String mTag;
    private final int mPosition;

    private Interceptor<T> mCallback;

    private final EmptyHandler mEmpty;

    WidgetHolder(T widget, Checker checker, Interceptor<T> callback, EmptyHandler handler,
                 String tag, int position) {
        mWidget= widget;
        mChecker = checker;
        mCallback = callback;
        mEmpty = handler;
        mTag = tag;
        mPosition = position;
    }

    WidgetHolder(T field, Checker rule, EmptyHandler handler, String tag, int position) {
        mWidget = field;
        mChecker = rule;
        mEmpty = handler;
        mTag = tag;
        mPosition = position;
    }

    @Deprecated
    public String value() {
        return mWidget.content();
    }

    public void showError(String message) {
        mWidget.showError(message);
    }

    void connect(ViewGroup group, int size) {
        group.addView(mWidget);
        if (mPosition == 0) {
            mWidget.position(FormWidget.FIRST);
        } else if (mPosition < size) {
            mWidget.position(FormWidget.MIDDLE);
        } else {
            mWidget.position(FormWidget.LAST);
        }
        mCallback.add(mWidget);
    }

    void bind() {
        String add = mCallback.bind();
        mWidget.bind(add);
    }

    void setCallback(Interceptor<T> value) {
        mCallback = value;
    }

    boolean isCallback() {
        return mCallback != null;
    }

    private String content() {
        return mWidget.content();
    }

    boolean verify() {
        String value = content();
        return mChecker.check(value);
    }

    void showError() {
        if (mChecker.error() == null) {
            throw new RuntimeException("showing before verify");
        }
        mWidget.showError(mChecker.error());
    }

    void hideError() {
        mWidget.showError(null);
    }

    String tag() {
        return mTag;
    }

    /*public*/ void enabled(boolean state) {
        mWidget.setEnabled(state);
    }

    public void visibility(int visibility) {
        mWidget.setVisibility(visibility);
    }

    void handleEmpty() {
        if (mEmpty != null) {
            mEmpty.process(this);
        }
    }
}
