package com.turlir.abakgists.base.loader;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;

public class SimpleLoader<T> {

    private final Pager<T> mDelegate;

    @NonNull
    private Window mInfo;

    public SimpleLoader(Pager<T> delegate, @NonNull Window seed) {
        mDelegate = delegate;
        mInfo = seed;
    }

    public Flowable<List<T>> currentPage() {
        return mDelegate.currentPage(mInfo);
    }

    public Flowable<List<T>> nextPage() {
        mInfo = mInfo.next();
        return currentPage();
    }

    public Flowable<List<T>> prevPage() {
        mInfo = mInfo.prev();
        return currentPage();
    }

    public Window getWindow() {
        return mInfo;
    }

    public void setWindow(Window w) {
        mInfo = w;
    }
}
