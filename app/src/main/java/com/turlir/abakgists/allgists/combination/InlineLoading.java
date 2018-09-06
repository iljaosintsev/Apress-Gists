package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

public class InlineLoading extends ListCombination<GistModel> {

    InlineLoading(ListCombination<GistModel> parent) {
        super(parent);
    }

    @Override
    public ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(this, items);
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
        owner.inlineLoad(true);
    }
}
