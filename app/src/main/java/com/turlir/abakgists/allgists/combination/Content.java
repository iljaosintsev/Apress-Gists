package com.turlir.abakgists.allgists.combination;

import java.util.List;

class Content<T> extends ListCombination<T> {

    private final List<T> mItems;

    Content(ListCombination<T> parent, List<T> items) {
        super(parent);
        mItems = items;
    }

    @Override
    public ListCombination<T> doLoad() {
        return new InlineLoading<>(this);
    }

    @Override
    public ListCombination<T> refresh() {
        return new Refresh<>(this); // with data
    }

    @Override
    public ListCombination<T> content(List<T> items) { // by design
        return new Content<>(this, items);
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
