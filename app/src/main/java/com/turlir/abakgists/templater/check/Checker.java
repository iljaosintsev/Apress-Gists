package com.turlir.abakgists.templater.check;

public interface Checker<V> {

    boolean check(V value);

    String error();

}
