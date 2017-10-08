package com.turlir.abakgists.templater.base;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBuilder<M> {

    private final List<WidgetHolder> mHolders;
    private final List<Out<M>> mOuts;
    private final Context mContext;

    public BaseBuilder(Context cnt) {
        mContext = cnt;
        mHolders = new ArrayList<>();
        mOuts = new ArrayList<>();
    }

    public Template<M> build() {
        return new Template<>(mHolders, mOuts);
    }

    protected <V extends View & FormWidget<T>, T> void add(Checker<T> rule, Interceptor<V, T> callback, V field) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule, callback);
        privateAdd(h);
    }

    protected <V extends View & FormWidget<T>, T> void add(Checker<T> rule, V field) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule);
        privateAdd(h);
    }

    protected final void interceptor(Out<M> o) {
        if (mHolders.size() > 0) {
            mOuts.set(mHolders.size() - 1, o);
        }
    }

    protected final <V extends View & FormWidget<T>, T> void interceptor(Interceptor<V, T> callback) {
        WidgetHolder h = mHolders.get(mHolders.size() - 1);
        //noinspection unchecked
        h.setCallback(callback);
    }

    protected final Context getContext() {
        return mContext;
    }

    private void privateAdd(WidgetHolder h) {
        if (mHolders.size() > 0) {
            WidgetHolder last = mHolders.get(mHolders.size() - 1);
            if (!last.isCallback()) {
                throw new IllegalStateException();
            }
        }
        mHolders.add(h);

        mOuts.add(null);
    }
}
