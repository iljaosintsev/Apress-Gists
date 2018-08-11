package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Content extends ListCombination<GistModel> {

    private final List<GistModel> mItems;

    Content(ListCombination<GistModel> parent, List<GistModel> items) {
        super(parent);
        mItems = items;
    }

    @Override
    public ListCombination<GistModel> doLoad() {
        return new InlineLoading(this);
    }

    @Override
    public ListCombination<GistModel> refresh() {
        return new Refresh(this); // with data
    }

    @Override
    public ListCombination<GistModel> content(List<GistModel> items) { // by design
        return new Content(this, items);
    }

    @Override
    public void perform() {
        super.perform();
        if (mItems.isEmpty()) {
            owner.emptyData(true);
        } else {
            owner.renderData(mItems);
        }
        owner.blockingLoad(false);
        owner.inlineLoad(false);
    }
}
