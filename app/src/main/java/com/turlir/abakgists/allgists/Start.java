package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorSelector;
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
    ListCombination<GistModel> error(Throwable err, ErrorSelector selector, ErrorProcessing processor) {
        return new Error(err, selector, processor);
    }

    @Override
    void perform(Callback<GistModel> call) {
        call.inlineLoad(false);
        call.blockingLoad(true);
    }
}
