package com.turlir.abakgists.allgists;

import android.os.Looper;
import android.support.annotation.CheckResult;

import java.util.List;

abstract class ListCombination<T> {

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
    ListCombination<T> refresh() {
        throw new IllegalStateException();
    }

    @CheckResult
    ListCombination<T> content(List<T> items) {
        throw new IllegalStateException();
    }

    @CheckResult
    ListCombination<T> error(Throwable err) {
        throw new IllegalStateException();
    }

    @CheckResult
    ListCombination<T> doLoad() {
        throw new IllegalStateException();
    }

    void perform() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("access view outside main thread");
        }
    }

}
