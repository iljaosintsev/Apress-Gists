package com.turlir.abakgists.templater.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public abstract class DynamicForm<T> implements Form<T> {

    private final ViewGroup mGroup;
    private final Context mContext;

    private Template<T> mTemplate;

    private T value;

    public DynamicForm(@NonNull ViewGroup group) {
        mGroup = group;
        mContext = group.getContext();
    }

    @Override
    public final void create() {
        mGroup.removeAllViews();
        mTemplate = createTemplate();
    }

    @Override
    public final void bind(@NonNull T value) {
        if (mTemplate == null) {
            throw new IllegalStateException("bind() before create()");
        }
        if (mGroup.getChildCount() < 1) {
            throw new IllegalStateException("bind() before connect()");
        }
        this.value = value;
        mTemplate.bind();
        interact();
    }

    public final void showError(String tag, String message) {
        mTemplate.showError(tag, message); // simple proxy, it`s bad ?
    }

    public final void enabled(String tag, boolean state) {
        mTemplate.enabled(tag, state);
    }

    public final void visibility(String tag, int visibility) {
        mTemplate.visibility(tag, visibility);
    }

    @Override
    public /*final*/ void connect() {
        if (mTemplate == null) {
            throw new IllegalStateException("connect() before create()");
        }
        mTemplate.connect(mGroup);
    }

    @Override
    public boolean verify() {
        if (mTemplate == null) {
            throw new IllegalStateException("verify() before create()");
        }
        return mTemplate.verify();
    }

    @NonNull
    @Override
    public T collect() {
        mTemplate.collect(value);
        return value;
    }

    protected final T value() {
        if (value == null) {
            throw new IllegalStateException();
        }
        return value;
    }

    protected final Context getContext() {
        return mContext;
    }

    protected abstract Template<T> createTemplate();

    protected abstract void interact();
}
