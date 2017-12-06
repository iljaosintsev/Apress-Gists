package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Start extends ListCombination<GistModel> {

    @Override
    ListCombination<GistModel> doLoad(int count) {
        return this;
    }

    @Override
    ListCombination<GistModel> content(List<GistModel> items) {
        if (!items.isEmpty()) {
            return new Content(items);
        } else {
            return new Empty();
        }
    }

    @Override
    ListCombination<GistModel> error(Throwable err, BasePresenter.ErrorHandler handler) {
        return new Error(err, handler);
    }

    @Override
    void perform(Callback<GistModel> call) {
        call.inlineLoad(false);
        call.blockingLoad(true);
    }
}
