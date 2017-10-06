package com.turlir.abakgists.templater.base;

import android.view.View;
import android.view.ViewGroup;

class WidgetHolder<T extends View & FormWidget<V>, V> {

    private final T mWidget;
    private Interceptor<T, V> mCallback;
    private final Checker<V> mChecker;

    WidgetHolder(T widget, Checker<V> checker, Interceptor<T, V> callback) {
        mWidget= widget;
        mChecker = checker;
        mCallback = callback;
    }

    WidgetHolder(T field, Checker<V> rule) {
        mWidget = field;
        mChecker = rule;
    }

    void connect(ViewGroup group) {
        group.addView(mWidget);
        mCallback.add(mWidget);
    }

    void bind() {
        V add = mCallback.bind();
        mWidget.bind(add);
    }

    void setCallback(Interceptor<T, V> value) {
        mCallback = value;
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
}
