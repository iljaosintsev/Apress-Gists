package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

public class Refresh extends ListCombination<GistModel> {

    Refresh(ListCombination<GistModel> parent) {
        super(parent);
    }

    @Override
    public ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(this, items);
    }

    @Override
    public ListCombination<GistModel> error(Throwable err) {
        com.turlir.abakgists.allgists.combination.Error error = new com.turlir.abakgists.allgists.combination.Error(err);
        error.setOwner(owner);
        return error;
    }

    @Override
    public ListCombination<GistModel> doIntermediate() {
        return new IntermediateState(this);
    }
}
