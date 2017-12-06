package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

class InlineLoading extends ListCombination<GistModel> {

    @Override
    ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(items);
    }

    @Override
    ListCombination<GistModel> error(Throwable err, BasePresenter.ErrorHandler handler) {
        return new Error(err, handler);
    }

    @Override
    void perform(Callback<GistModel> call) {
        call.inlineLoad(true);
    }
}
