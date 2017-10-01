package com.turlir.abakgists.templater;

import android.view.View;
import android.view.ViewGroup;

public class WidgetHolder<T extends View & FormWidget<V>, V> {

    private final T mWidget;
    private final V mContent;
    private final Callback<T> mCallback;
    private final Checker<V> mChecker;

    private String mLastError;

    public WidgetHolder(T widget, V content, Checker<V> checker, Callback<T> callback) {
        mWidget= widget;
        mContent = content;
        mChecker = checker;
        mCallback = callback;
    }

    public void connect(ViewGroup group) {
        group.addView(mWidget);
        mCallback.added(mWidget);
    }

    public void bind() {
        mWidget.bind(mContent);
    }

    public V content() {
        return mWidget.content();
    }

    public boolean verify() {
        V value = content();
        boolean result = mChecker.check(value);
        if (!result) {
            mLastError = mChecker.error();
            return false;
        } else {
            return true;
        }
    }

    public void showError() {
        if (mLastError == null) {
            throw new RuntimeException("showing before verify");
        }
        mWidget.showError(mLastError);
    }

    public void hideError() {
        mWidget.showError(null);
    }
}
