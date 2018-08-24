package com.turlir.abakgists.base.erroring;

import android.support.annotation.NonNull;

public final class RepeatingError extends CommonError {

    @Override
    public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
        return isErrorNow;
    }

    @Override
    public void perform(@NonNull ErrorInterpreter v, Exception e) {
        super.perform(v, e);
        v.blockingError("Увы, попытайтесь снова через некоторое время");
    }
}
