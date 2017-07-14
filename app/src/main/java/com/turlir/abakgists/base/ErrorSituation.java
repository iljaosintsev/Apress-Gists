package com.turlir.abakgists.base;

public interface ErrorSituation {

    boolean should(Exception ex, boolean dataAvailable, boolean isErrorNow);

    void perform(ErrorInterpreter v, Exception e);
}
