package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

public class Start extends ListCombination<GistModel> {

    public Start(ListManipulator<GistModel> owner) {
        setOwner(owner);
    }

    @Override
    public ListCombination<GistModel> content(List<GistModel> items) {
        if (!items.isEmpty()) {
            return new Content(this, items);
        } else {
            return new Empty(this);
        }
    }

    @Override
    public ListCombination<GistModel> error(Throwable err, ErrorProcessor processor) {
        Error error = new Error(err, processor);
        error.setOwner(owner);
        return error;
    }

    @Override
    public ListCombination<GistModel> doIntermediate() {
        return new IntermediateState(this);
    }

    @Override
    public void perform() {
        super.perform();
        owner.inlineLoad(false);
        owner.blockingLoad(true);
    }
}
