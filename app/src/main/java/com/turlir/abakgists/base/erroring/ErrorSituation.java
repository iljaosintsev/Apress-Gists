package com.turlir.abakgists.base.erroring;

import android.content.res.Resources;
import androidx.annotation.NonNull;

public interface ErrorSituation {

    boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow);

    void perform(@NonNull ErrorInterpreter v, Exception e, Resources res);
}
