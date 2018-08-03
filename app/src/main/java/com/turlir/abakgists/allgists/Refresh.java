package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Refresh extends ListCombination<GistModel> {

    Refresh(ListCombination<GistModel> parent) {
        super(parent);
    }

    @Override
    ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(this, items);
    }

    @Override
    ListCombination<GistModel> error(Throwable err, ErrorSelector selector, ErrorProcessing processor) {
        Error error = new Error(err, selector, processor);
        error.setOwner(owner);
        return error;
    }
}
