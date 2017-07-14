package com.turlir.abakgists.base;


import android.support.annotation.NonNull;

import java.io.IOException;

import timber.log.Timber;

class TroubleSelector {

    private final ErrorSituation mTrueTrue = new CommonError() { // 3
        @Override
        public void perform(ErrorInterpreter v, Exception e) {
            v.nonBlockingError("Отсутствует подключение к сети Интернет");
        }
    };

    private final ErrorSituation mTrueFalse = new CommonError() { // 2
        @Override
        public void perform(ErrorInterpreter v, Exception e) {
            v.blockingError("Ошибка " + getErrorName(e) + " при получении данных");
        }
    };

    private final ErrorSituation mFalseTrue = new CommonError() { // 1
        @Override
        public void perform(ErrorInterpreter v, Exception e) {
            v.alertError("Произошла ошибка" + "\n" + getErrorName(e));
        }
    };

    private final ErrorSituation mFalseFalse = new CommonError() { // 0
        @Override
        public void perform(ErrorInterpreter v, Exception e) {
            v.blockingError("Ошибка " + getErrorName(e));
        }
    };

    private final ErrorSituation[] mVariants = new ErrorSituation[] {
            mFalseFalse, mFalseTrue, mTrueFalse, mTrueTrue
    };

    private final ErrorSituation[] mCallbacks;

    TroubleSelector(ErrorSituation[] callbacks) {
        mCallbacks = callbacks;
    }

    @NonNull
    ErrorSituation select(Exception ex, boolean dataAvailable, boolean isError) {
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

    private abstract static class CommonError implements ErrorSituation {

        @Override
        public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
            return false;
        }

        @Override
        public void perform(ErrorInterpreter v, Exception ex) {
            Timber.e(ex);
        }

        static String getErrorName(Exception ex) {
            return ex.getClass().getSimpleName();
        }
    }

}
