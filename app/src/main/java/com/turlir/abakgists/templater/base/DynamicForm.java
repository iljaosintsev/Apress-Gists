package com.turlir.abakgists.templater.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public abstract class DynamicForm<T> implements Form<T> {

    private final ViewGroup mGroup;
    private final Context mContext;

    private Template mTemplate;

    protected T value;

    public DynamicForm(@NonNull ViewGroup group) {
        mGroup = group;
        mContext = group.getContext();
    }

    @Override
    public final void create() {
        mTemplate = createTemplate();
    }

    @Override
    public final void connect() {
        if (mTemplate == null) {
            throw new IllegalStateException("connect() before create()");
        }
        mTemplate.connect(mGroup);
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

    @Override
    public boolean verify() {
        if (mTemplate == null) {
            throw new IllegalStateException("verify() before create()");
        }
        return mTemplate.verify();
    }

    protected final T value() {
        if (value == null) {
            throw new IllegalStateException();
        }
        return value;
    }

    protected abstract Template createTemplate();

    protected abstract void interact();

    protected final Context getContext() {
        return mContext;
    }
}
