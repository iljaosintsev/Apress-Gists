package com.turlir.abakgists.templater.base;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BaseBuilder {

    private final List<WidgetHolder> mHolders;
    private final Context mContext;

    public BaseBuilder(Context cnt) {
        mContext = cnt;
        mHolders = new ArrayList<>();
    }

    public Template build() {
        return new Template(mHolders);
    }

    protected <V extends View & FormWidget<T>, T> void add(Checker<T> rule, Interceptor<V, T> callback, V field) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule, callback);
        mHolders.add(h);
    }

    protected <V extends View & FormWidget<T>, T> void add(Checker<T> rule, V field) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule);
        mHolders.add(h);
    }

    protected final <V extends View & FormWidget<T>, T> void interceptor(Interceptor<V, T> callback) {
        WidgetHolder h = mHolders.get(mHolders.size() - 1);
        //noinspection unchecked
        h.setCallback(callback);
    }

    protected final Context getContext() {
        return mContext;
    }
}
