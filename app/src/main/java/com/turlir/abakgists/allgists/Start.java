package com.turlir.abakgists.allgists;

import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Start extends ListCombination<GistModel> {

    Start(ListManipulator<GistModel> owner) {
        setOwner(owner);
    }

    @Override
    ListCombination<GistModel> content(List<GistModel> items) {
        if (!items.isEmpty()) {
            return new Content(this, items);
        } else {
            return new Empty(this);
        }
    }

    @Override
    ListCombination<GistModel> error(Throwable err) {
        Error error = new Error(err);
        error.setOwner(owner);
        return error;
    }

    @Override
    void perform() {
        super.perform();
        owner.inlineLoad(false);
        owner.blockingLoad(true);
    }
}
