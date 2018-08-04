package com.turlir.abakgists.allgists;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Empty extends ListCombination<GistModel> {

    Empty(ListCombination<GistModel> parent) {
        super(parent);
    }

    @Override
    ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(this, items);
    }

    @Override
    void perform() {
        super.perform();
        owner.inlineLoad(false);
        owner.blockingLoad(false);
        owner.emptyData(true);
    }
}
