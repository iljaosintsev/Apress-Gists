package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.BasePresenter;
import com.turlir.abakgists.model.GistModel;

class Error extends ListCombination<GistModel> {

    private final Throwable mError;
    private final BasePresenter.ErrorHandler mHandler;

    Error(Throwable error, BasePresenter.ErrorHandler handler) {
        mError = error;
        mHandler = handler;
    }

    @Override
    ListCombination<GistModel> refresh() {
        return new Refresh();
    }

    @Override
    void perform(Callback<GistModel> call) {
        call.inlineLoad(false);
        call.blockingLoad(false);

        mHandler.onError(mError);
    }
}
