package com.turlir.abakgists.templater.check;

public class TrueCheck<T> implements Checker {

    @Override
    public boolean check(String value) {
        return true;
    }

    @Override
    public String error() {
        return null;
    }
}
