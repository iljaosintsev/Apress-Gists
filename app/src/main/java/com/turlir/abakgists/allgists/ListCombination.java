package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

abstract class ListCombination<T> {

    ListCombination<GistModel> refresh() {
        throw new IllegalStateException();
    }

    ListCombination<GistModel> content(List<T> items) {
        throw new IllegalStateException();
    }

    ListCombination<GistModel> error(Throwable err, ErrorSelector selector) {
        throw new IllegalStateException();
    }

    ListCombination<GistModel> doLoad(int count) {
        throw new IllegalStateException();
    }

    void perform(Callback<T> call) {
        //
    }

    interface Callback<T> extends ErrorProcessing {

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
