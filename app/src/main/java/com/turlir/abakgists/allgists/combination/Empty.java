package com.turlir.abakgists.allgists.combination;

import java.util.List;

class Empty<T> extends ListCombination<T> {

    Empty(ListCombination<T> parent) {
        super(parent);
    }

    @Override
    public ListCombination<T> content(List<T> items) {
        return new Content<>(this, items);
    }

    @Override
    public void perform() {
        super.perform();
        owner.inlineLoad(false);
        owner.blockingLoad(false);
        owner.emptyData(true);
    }
}
