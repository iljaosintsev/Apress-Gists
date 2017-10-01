package com.turlir.abakgists.templater;

public interface Checker<V> {

    boolean check(V value);

    String error();

}
