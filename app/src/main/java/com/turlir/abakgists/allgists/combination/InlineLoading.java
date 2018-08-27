package com.turlir.abakgists.allgists.combination;

import java.util.List;

public class InlineLoading<T> extends ListCombination<T> {

    InlineLoading(ListCombination<T> parent) {
        super(parent);
    }

    @Override
    public ListCombination<T> content(List<T> items) {
        return new Content<>(this, items);
    }

    @Override
    public ListCombination<T> error(Throwable err) {
        Error<T> error = new Error<>(err);
        error.setOwner(owner);
        return error;
    }

    @Override
    public ListCombination<T> doIntermediate() {
        return new IntermediateState<>(this);
    }

    @Override
    public void perform() {
        super.perform();
        owner.inlineLoad(true);
    }
}
