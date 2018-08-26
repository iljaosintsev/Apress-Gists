package com.turlir.abakgists.model;

public interface Identifiable<T> {

    String getId();

    boolean isDifferent(T o);

}
