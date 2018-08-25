package com.turlir.abakgists.base.erroring;

import android.content.res.Resources;
import android.support.annotation.NonNull;

public final class RepeatingError extends CommonError {

    @Override
    public boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow) {
        return isErrorNow;
    }

    @Override
    public void perform(@NonNull ErrorInterpreter v, Exception e, Resources res) {
        super.perform(v, e, res);
        v.blockingError("Увы, попытайтесь снова через некоторое время");
    }
}
