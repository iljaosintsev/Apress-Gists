package com.turlir.abakgists.templater.check;

public class TrueCheck<T> implements Checker<T> {

    @Override
    public boolean check(Object value) {
        return true;
    }

    @Override
    public String error() {
        return null;
    }
}
