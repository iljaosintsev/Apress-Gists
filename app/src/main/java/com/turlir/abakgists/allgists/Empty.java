package com.turlir.abakgists.allgists;

import com.turlir.abakgists.model.GistModel;

class Empty extends ListCombination<GistModel> {

    @Override
    void perform(Callback<GistModel> call) {
        call.inlineLoad(false);
        call.blockingLoad(false);
        call.emptyData(true);
    }
}
