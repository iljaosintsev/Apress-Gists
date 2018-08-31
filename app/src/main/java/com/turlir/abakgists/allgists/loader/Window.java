package com.turlir.abakgists.allgists.loader;

public interface Window {

    int start();

    int stop();

    int addition();

    int count();

    //

    Range downScale(int coefficient);

    //

    Window next();

    Window prev();

    boolean hasNext();

    boolean hasPrevious();
}
