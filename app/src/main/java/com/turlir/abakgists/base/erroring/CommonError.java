package com.turlir.abakgists.base.erroring;

import android.content.res.Resources;
import androidx.annotation.NonNull;

import timber.log.Timber;

abstract class CommonError implements ErrorSituation {

    @Override
    public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
        return false;
    }

    @Override
    public void perform(@NonNull ErrorInterpreter v, Exception ex, Resources res) {
        Timber.e(ex);
    }

    static String getErrorName(Exception ex) {
        return ex.getClass().getSimpleName();
    }
}
