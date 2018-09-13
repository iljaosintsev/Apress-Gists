package com.turlir.abakgists.allgists.combination;

import java.util.List;

public class Start<T> extends ListCombination<T> {

    public Start(ListManipulator<T> owner) {
        setOwner(owner);
    }

    @Override
    public ListCombination<T> content(List<T> items) {
        if (!items.isEmpty()) {
            return new Content<>(this, items);
        } else {
            return new Empty<>(this);
        }
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

    @Override
    public ListCombination<T> doLoad() {
        return this;
    }

    @Override
    public void perform() {
        super.perform();
        owner.inlineLoad(false);
        owner.blockingLoad(true);
    }
}
