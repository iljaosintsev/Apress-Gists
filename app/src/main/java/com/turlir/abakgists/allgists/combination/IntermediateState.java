package com.turlir.abakgists.allgists.combination;

import java.util.List;

class IntermediateState<T> extends ListCombination<T> {

    private final ListCombination<T> mDelegate;

    IntermediateState(ListCombination<T> parent) {
        super(parent);
        mDelegate = parent;
    }

    @Override
    public ListCombination<T> content(List<T> items) {
        return mDelegate.content(items);
    }

    @Override
    public ListCombination<T> error(Throwable err, ErrorProcessor processor) {
        return mDelegate.error(err, processor);
    }
}
