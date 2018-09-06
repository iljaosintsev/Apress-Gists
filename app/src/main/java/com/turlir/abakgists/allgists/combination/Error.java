package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;

import java.util.List;

class Error<T> extends ListCombination<T> {

    private final Throwable mError;
    private final ErrorProcessor mProcessor;

    Error(Throwable error, ErrorProcessor processor) {
        mError = error;
        mProcessor = processor;
    }

    @Override
    public ListCombination<T> refresh() {
        return new Refresh<>(this);
    }

    @Override
    public ListCombination<T> content(List<T> items) {
        return new Content<>(this, items);
    }

    @Override
    public void perform() {
        super.perform();
        ErrorSelector selector = mProcessor.getErrorSelector();
        ErrorInterpreter interpreter = mProcessor.interpreter();
        if (interpreter == null) return;

        if (mError instanceof Exception) {
            owner.inlineLoad(false);
            owner.blockingLoad(false);

            Exception exception = (Exception) mError;
            boolean isData = mProcessor.dataAvailable();
            boolean isError = mProcessor.isError();
            ErrorSituation situation = selector.select(exception, isData, isError);
            situation.perform(interpreter, exception, mProcessor.getResources());

        } else {
            throw new RuntimeException(mError);
        }
    }
}
