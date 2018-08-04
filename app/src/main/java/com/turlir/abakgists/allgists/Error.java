package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.model.GistModel;

class Error extends ListCombination<GistModel> {

    private final Throwable mError;
    private final ErrorSelector mSelector;
    private final ErrorProcessor mProcessor;

    Error(Throwable error, ErrorSelector selector, ErrorProcessor processor) {
        mError = error;
        mSelector = selector;
        mProcessor = processor;
    }

    @Override
    ListCombination<GistModel> refresh() {
        return new Refresh(this);
    }

    @Override
    void perform() {
        super.perform();
        if (mError instanceof Exception) {
            owner.inlineLoad(false);
            owner.blockingLoad(false);

            Exception exception = (Exception) mError;
            boolean isData = mProcessor.dataAvailable();
            boolean isError = mProcessor.isError();
            ErrorSituation situation = mSelector.select(exception, isData, isError);
            situation.perform(mProcessor.interpreter(), exception);

        } else {
            throw new RuntimeException(mError);
        }
    }
}
