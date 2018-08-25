package com.turlir.abakgists.allgists.combination;

import android.os.Looper;
import android.support.annotation.CheckResult;

import java.util.List;

public abstract class ListCombination<T> {

    protected ListManipulator<T> owner;

    ListCombination() {

    }

    ListCombination(ListCombination<T> parent) {
        setOwner(parent.owner);
    }

    void setOwner(ListManipulator<T> owner) {
        this.owner = owner;
    }

    @CheckResult
    public ListCombination<T> refresh() {
        throw new IllegalStateException();
    }

    @CheckResult
    public ListCombination<T> content(List<T> items) {
        throw new IllegalStateException();
    }

    @CheckResult
    public ListCombination<T> error(Throwable err) {
        throw new IllegalStateException(err);
    }

    @CheckResult
    public ListCombination<T> doLoad() {
        throw new IllegalStateException();
    }

    @CheckResult
    public ListCombination<T> doIntermediate() {
        throw new IllegalStateException();
    }

    public void perform() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("access view outside main thread");
        }
    }

}
