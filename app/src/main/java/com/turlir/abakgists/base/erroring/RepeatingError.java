package com.turlir.abakgists.base.erroring;

import android.support.annotation.NonNull;

public final class RepeatingError implements ErrorSituation {

    @Override
    public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
        return isErrorNow;
    }

    @Override
    public void perform(@NonNull ErrorInterpreter v, Exception e) {
        v.blockingError("Увы, попытайтесь снова через некоторое время");
    }
}
