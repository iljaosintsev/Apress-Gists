package com.turlir.abakgists.allgists.loader;

public interface Window {

    int start();

    int stop();

    int addition();

    int count();

    //

    Window cut(int size);

    Window diff(Window o);

    LoadablePage page();

    //

    Window next();

    Window prev();

    boolean hasNext();

    boolean hasPrevious();
}
