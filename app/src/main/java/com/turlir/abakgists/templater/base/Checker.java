package com.turlir.abakgists.templater.base;

public interface Checker<V> {

    boolean check(V value);

    String error();

}
