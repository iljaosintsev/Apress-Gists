package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;

import java.util.List;

abstract class ListCombination<T> {

    ListCombination<T> refresh() {
        throw new IllegalStateException();
    }

    ListCombination<T> content(List<T> items) {
        throw new IllegalStateException();
    }

    ListCombination<T> error(Throwable err, ErrorSelector selector, ErrorProcessing processor) {
        throw new IllegalStateException();
    }

    ListCombination<T> doLoad(int count) {
        throw new IllegalStateException();
    }

    void perform(Callback<T> call) {
        //
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
