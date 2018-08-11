package com.turlir.abakgists.allgists.combination;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;
import com.turlir.abakgists.base.erroring.ErrorSelector;

public interface ErrorProcessor {

    ErrorSelector getErrorSelector();

    ErrorInterpreter interpreter();

    boolean dataAvailable();

    boolean isError();
}
