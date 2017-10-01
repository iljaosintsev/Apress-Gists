package com.turlir.abakgists.templater.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public abstract class DynamicForm<T> implements Form<T> {

    private final ViewGroup mGroup;
    private final Context mContext;

    private Template mTemplate;

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
        mTemplate.bind();
    }

    @Override
    public void bind(@NonNull T value) {
        if (mTemplate == null) {
            throw new IllegalStateException("connect() before create()");
        }
        if (mGroup.getChildCount() < 1) {
            throw new IllegalStateException("bind() before connect()");
        }
    }

    @Override
    public boolean verify() {
        if (mTemplate == null) {
            throw new IllegalStateException("verify() before create()");
        }
        return mTemplate.verify();
    }

    protected abstract Template createTemplate();

    protected final Context getContext() {
        return mContext;
    }
}
