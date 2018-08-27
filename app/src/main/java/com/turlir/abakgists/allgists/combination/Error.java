package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;

import java.util.List;

class Error<T> extends ListCombination<T> {

    private final Throwable mError;

    Error(Throwable error) {
        mError = error;
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
        ErrorProcessor processor = owner.getErrorProcessor();
        ErrorSelector selector = processor.getErrorSelector();
        ErrorInterpreter interpreter = processor.interpreter();
        if (interpreter == null) return;

        if (mError instanceof Exception) {
            owner.inlineLoad(false);
            owner.blockingLoad(false);

            Exception exception = (Exception) mError;
            boolean isData = processor.dataAvailable();
            boolean isError = processor.isError();
            ErrorSituation situation = selector.select(exception, isData, isError);
            situation.perform(interpreter, exception, processor.getResources());

        } else {
            throw new RuntimeException(mError);
        }
    }
}
