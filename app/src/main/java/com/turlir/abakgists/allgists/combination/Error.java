package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;
import com.turlir.abakgists.base.erroring.ErrorSituation;
import com.turlir.abakgists.model.GistModel;

import java.util.List;

class Error extends ListCombination<GistModel> {

    private final Throwable mError;

    Error(Throwable error) {
        mError = error;
    }

    @Override
    public ListCombination<GistModel> refresh() {
        return new Refresh(this);
    }

    @Override
    public ListCombination<GistModel> content(List<GistModel> items) {
        return new Content(this, items);
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
            situation.perform(interpreter, exception);

        } else {
            throw new RuntimeException(mError);
        }
    }
}
