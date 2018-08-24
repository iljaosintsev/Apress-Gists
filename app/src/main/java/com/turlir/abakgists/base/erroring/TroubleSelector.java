package com.turlir.abakgists.base.erroring;


import android.support.annotation.NonNull;

import java.io.IOException;

public class TroubleSelector implements ErrorSelector {

    private final ErrorSituation mTrueTrue = new CommonError() { // 3
        @Override
        public void perform(@NonNull ErrorInterpreter v, Exception e) {
            super.perform(v, e);
            v.nonBlockingError("Отсутствует подключение к сети Интернет");
        }
    };

    private final ErrorSituation mTrueFalse = new CommonError() { // 2
        @Override
        public void perform(@NonNull ErrorInterpreter v, Exception e) {
            super.perform(v, e);
            v.blockingError("Ошибка " + getErrorName(e) + " при получении данных");
        }
    };

    private final ErrorSituation mFalseTrue = new CommonError() { // 1
        @Override
        public void perform(@NonNull ErrorInterpreter v, Exception e) {
            super.perform(v, e);
            v.alertError("Произошла ошибка" + "\n" + getErrorName(e));
        }
    };

    private final ErrorSituation mFalseFalse = new CommonError() { // 0
        @Override
        public void perform(@NonNull ErrorInterpreter v, Exception e) {
            super.perform(v, e);
            v.blockingError("Ошибка " + getErrorName(e));
        }
    };

    private final ErrorSituation[] mVariants = new ErrorSituation[] {
            mFalseFalse, mFalseTrue, mTrueFalse, mTrueTrue
    };

    private final ErrorSituation[] mCallbacks;

    public TroubleSelector(ErrorSituation...callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    @NonNull
    public ErrorSituation select(Exception ex, boolean dataAvailable, boolean isError) {
        for (ErrorSituation item : mCallbacks) {
            boolean should = item.should(ex, dataAvailable, isError);
            if (should) {
                return item;
            }
        }
        int i = index(ex instanceof IOException, dataAvailable);
        return mVariants[i];
    }

    /**
     * выбор подходящей под ситуации стратегии
     * @param isNetwork второй бит
     * @param dataAvailable первый бит
     * @return индекс стратегии в списке
     */
    private int index(boolean isNetwork, boolean dataAvailable) {
        int index = 0;
        if (isNetwork) index = index | (1 << 1);
        if (dataAvailable) index = index | 1;
        return index;
    }

}
