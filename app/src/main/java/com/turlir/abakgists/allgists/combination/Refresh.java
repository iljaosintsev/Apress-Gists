package com.turlir.abakgists.allgists.combination;

import java.util.List;

public class Refresh<T> extends ListCombination<T> {

    Refresh(ListCombination<T> parent) {
        super(parent);
    }

    @Override
    public ListCombination<T> content(List<T> items) {
        return new Content<>(this, items);
    }

    @Override
    public ListCombination<T> error(Throwable err, ErrorProcessor processor) {
        Error<T> error = new Error<>(err, processor);
        error.setOwner(owner);
        return error;
    }

    @Override
    public ListCombination<T> doIntermediate() {
        return new IntermediateState<>(this);
    }
}
