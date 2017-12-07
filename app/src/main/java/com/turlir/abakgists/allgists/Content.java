package com.turlir.abakgists.allgists;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Content extends ListCombination<GistModel> {

    private final List<GistModel> mItems;

    Content(List<GistModel> items) {
        mItems = items;
    }

    @Override
    ListCombination<GistModel> doLoad(int count) {
        return new InlineLoading();
    }

    @Override
    ListCombination<GistModel> refresh() {
        return new Refresh(); // with data
    }

    @Override
    ListCombination<GistModel> content(List<GistModel> items) { // database first designs
        return new Content(items);
    }

    @Override
    void perform(Callback<GistModel> call) {
        if (mItems.isEmpty()) {
            call.emptyData(true);
        } else {
            call.renderData(mItems);
        }
        call.blockingLoad(false);
        call.inlineLoad(false);
    }
}
