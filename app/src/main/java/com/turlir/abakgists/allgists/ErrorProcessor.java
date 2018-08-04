package com.turlir.abakgists.allgists;

import com.turlir.abakgists.base.erroring.ErrorInterpreter;

interface ErrorProcessor {

    ErrorInterpreter interpreter();

    boolean dataAvailable();

    boolean isError();
}
