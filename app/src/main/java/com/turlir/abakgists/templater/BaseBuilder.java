package com.turlir.abakgists.templater;

import android.content.Context;
import android.view.View;

import com.turlir.abakgists.templater.base.Interceptor;
import com.turlir.abakgists.templater.base.Out;
import com.turlir.abakgists.templater.check.Checker;
import com.turlir.abakgists.templater.widget.FormWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBuilder<M, B extends BaseBuilder<M, B>> {

    private final List<WidgetHolder> mHolders;
    private final List<Out<M>> mOuts;
    private final Context mContext;

    public BaseBuilder(Context cnt) {
        mContext = cnt;
        mHolders = new ArrayList<>();
        mOuts = new ArrayList<>();
    }

    public final <V extends View & FormWidget<T>, T> B in(Interceptor<V, T> callback) {
        WidgetHolder h = mHolders.get(mHolders.size() - 1);
        //noinspection unchecked
        h.setCallback(callback);
        return getThis();
    }

    public final B out(Out<M> o) {
        if (mHolders.size() > 0) {
            mOuts.set(mHolders.size() - 1, o);
        } else {
            throw new IllegalStateException();
        }
        return getThis();
    }

    public final Template<M> build() {
        checkLastHolder();
        return new Template<>(mHolders, mOuts);
    }

    protected abstract B getThis();

    protected final Context getContext() {
        return mContext;
    }

    protected final <V extends View & FormWidget<T>, T> void add(Checker<T> rule, Interceptor<V, T> callback,
                                                                 V field, String tag) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule, callback, tag, mHolders.size());
        privateAdd(h);
    }

    protected final <V extends View & FormWidget<T>, T> void add(Checker<T> rule, V field, String tag) {
        WidgetHolder<V, T> h = new WidgetHolder<>(field, rule, tag, mHolders.size());
        privateAdd(h);
    }

    private void privateAdd(WidgetHolder h) {
        checkLastHolder();
        mHolders.add(h);
        mOuts.add(null);
    }

    private void checkLastHolder() {
        if (mHolders.size() > 0) {
            WidgetHolder last = mHolders.get(mHolders.size() - 1);
            if (!last.isCallback()) {
                throw new IllegalStateException();
            }
        }
    }
}
