package com.turlir.abakgists.allgists.combination;

import android.content.res.Resources;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;

public interface ErrorProcessor {

    ErrorSelector getErrorSelector();

    ErrorInterpreter interpreter();

    boolean dataAvailable();

    boolean isError();

    Resources getResources();

    void resetError();
}
