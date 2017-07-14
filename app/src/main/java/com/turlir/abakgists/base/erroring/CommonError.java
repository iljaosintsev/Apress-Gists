package com.turlir.abakgists.base.erroring;

import timber.log.Timber;

abstract class CommonError implements ErrorSituation {

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
