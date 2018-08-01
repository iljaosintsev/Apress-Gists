package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.model.GistModel;

class Error extends ListCombination<GistModel> {

    private final Throwable mError;
    private final ErrorSelector mSelector;
    private final ErrorProcessing mProcessor;

    Error(Throwable error, ErrorSelector selector, ErrorProcessing processor) {
        mError = error;
        mSelector = selector;
        mProcessor = processor;
    }

    @Override
    ListCombination<GistModel> refresh() {
        return new Refresh();
    }

    @Override
    void perform(Callback<GistModel> call) {
        if (mError instanceof Exception) {
            call.inlineLoad(false);
            call.blockingLoad(false);

            Exception exception = (Exception) mError;
            boolean isData = mProcessor.dataAvailable();
            boolean isError = mProcessor.isError();
            ErrorSituation situation = mSelector.select(exception, isData, isError);
            situation.perform(mProcessor.error(), exception);

        } else {
            throw new RuntimeException(mError);
        }
    }
}
