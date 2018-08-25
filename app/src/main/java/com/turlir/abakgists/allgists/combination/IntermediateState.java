package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

class IntermediateState extends ListCombination<GistModel> {

    private final ListCombination<GistModel> mDelegate;

    IntermediateState(ListCombination<GistModel> parent) {
        super(parent);
        mDelegate = parent;
    }

    @Override
    public ListCombination<GistModel> content(List<GistModel> items) {
        return mDelegate.content(items);
    }

    @Override
    public ListCombination<GistModel> error(Throwable err) {
        return mDelegate.error(err);
    }
}
