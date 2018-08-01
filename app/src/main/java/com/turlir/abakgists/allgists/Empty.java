package com.turlir.abakgists.allgists;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Empty extends ListCombination<GistModel> {

    @Override
    ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(items);
    }

    @Override
    void perform(Callback<GistModel> call) {
        call.inlineLoad(false);
        call.blockingLoad(false);
        call.emptyData(true);
    }
}
