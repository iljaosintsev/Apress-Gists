package com.turlir.abakgists.base;


import android.support.annotation.NonNull;

import java.io.IOException;

public class TroubleSelector {

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

    public TroubleSelector(ErrorSituation[] callbacks) {
        mCallbacks = callbacks;
    }

    @NonNull
    public ErrorSituation select(Exception throwable, boolean dataAvailable, boolean isError) {
        for (ErrorSituation item : mCallbacks) {
            boolean should = item.should(throwable, dataAvailable, isError);
            if (should) {
                return item;
            }
        }
        int i = index(throwable instanceof IOException, dataAvailable);
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

    public interface ErrorSituation {

        boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow);

        void perform(ErrorInterpreter v, Exception e);
    }

    public interface ErrorInterpreter {

        /**
         * Не блокирующая ошибка, когда контент уже есть
         * @param msg описание ситуации
         */
        void nonBlockingError(String msg);

        /**
         * Случайная ошибка, когда определить дальнейшие действия нельзя. Например NPE
         * @param msg описание ситуации
         */
        void alertError(String msg);

        /**
         * Блокирующая, когда данных нет или дальнейшая работа невозможна
         * @param msg описание ситуации
         */
        void blockingError(String msg);
    }

    private abstract static class CommonError implements ErrorSituation {

        @Override
        public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
            return false;
        }

        static String getErrorName(Exception ex) {
            return ex.getClass().getSimpleName();
        }
    }

}
