package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;

interface ErrorProcessor {

    ErrorSelector getErrorSelector();

    ErrorInterpreter interpreter();

    boolean dataAvailable();

    boolean isError();
}
