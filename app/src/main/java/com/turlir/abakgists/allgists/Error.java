package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.model.GistModel;

import timber.log.Timber;

class Error extends ListCombination<GistModel> {

    private final Throwable mError;
    private final ErrorSelector mSelector;

    Error(Throwable error, ErrorSelector selector) {
        mError = error;
        mSelector = selector;
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
            Timber.e(exception);
            ErrorSituation situation = mSelector.select(exception, call.dataAvailable(), call.isError());
            situation.perform(call.error(), exception);

        } else {
            throw new RuntimeException(mError);
        }

        // mHandler.onError(mError);
    }
}
