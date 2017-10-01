package com.turlir.abakgists.templater.base;

import android.view.View;
import android.view.ViewGroup;

class WidgetHolder<T extends View & FormWidget<V>, V> {

    private final T mWidget;
    private final V mContent;
    private final Interceptor<T> mCallback;
    private final Checker<V> mChecker;

    private String mLastError;

    WidgetHolder(T widget, V content, Checker<V> checker, Interceptor<T> callback) {
        mWidget= widget;
        mContent = content;
        mChecker = checker;
        mCallback = callback;
    }

    void connect(ViewGroup group) {
        group.addView(mWidget);
        mCallback.add(mWidget);
    }

    void bind() {
        mWidget.bind(mContent);
    }

    private V content() {
        return mWidget.content();
    }

    boolean verify() {
        V value = content();
        boolean result = mChecker.check(value);
        if (!result) {
            mLastError = mChecker.error();
            return false;
        } else {
            return true;
        }
    }

    void showError() {
        if (mLastError == null) {
            throw new RuntimeException("showing before verify");
        }
        mWidget.showError(mLastError);
    }

    void hideError() {
        mWidget.showError(null);
    }
}
