package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Refresh extends ListCombination<GistModel> {

    @Override
    ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(items);
    }

    @Override
    ListCombination<GistModel> error(Throwable err, ErrorSelector selector, ErrorProcessing processor) {
        return new Error(err, selector, processor);
    }
}
