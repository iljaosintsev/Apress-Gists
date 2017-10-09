package com.turlir.abakgists.templater.base;

import android.view.View;
import android.view.ViewGroup;

class WidgetHolder<T extends View & FormWidget<V>, V> {

    private final T mWidget;
    private final Checker<V> mChecker;
    private final int mPosition;

    private Interceptor<T, V> mCallback;

    WidgetHolder(T widget, Checker<V> checker, Interceptor<T, V> callback, int position) {
        mWidget= widget;
        mChecker = checker;
        mCallback = callback;
        mPosition = position;
    }

    WidgetHolder(T field, Checker<V> rule, int position) {
        mWidget = field;
        mChecker = rule;
        mPosition = position;
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
        V add = mCallback.bind();
        mWidget.bind(add);
    }

    void setCallback(Interceptor<T, V> value) {
        mCallback = value;
    }

    boolean isCallback() {
        return mCallback != null;
    }

    private V content() {
        return mWidget.content();
    }

    boolean verify() {
        V value = content();
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

    public String value() {
        return mWidget.content().toString();
    }
}
