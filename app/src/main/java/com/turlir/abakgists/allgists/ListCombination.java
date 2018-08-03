package com.turlir.abakgists.allgists;

import android.support.annotation.CheckResult;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;

import java.util.List;

abstract class ListCombination<T> {

    protected Callback<T> owner;

    ListCombination() {

    }

    ListCombination(ListCombination<T> parent) {
        setOwner(parent.owner);
    }

    void setOwner(Callback<T> owner) {
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
    ListCombination<T> error(Throwable err, ErrorSelector selector, ErrorProcessing processor) {
        throw new IllegalStateException();
    }

    @CheckResult
    ListCombination<T> doLoad() {
        throw new IllegalStateException();
    }

    void perform() {
        throw new IllegalStateException();
    }

    interface Callback<T> {

        void blockingLoad(boolean visible);

        void inlineLoad(boolean visible);

        void renderData(List<T> items);

        void emptyData(boolean visible);
    }

    interface ErrorProcessing {

        ErrorInterpreter error();

        boolean dataAvailable();

        boolean isError();
    }

}
