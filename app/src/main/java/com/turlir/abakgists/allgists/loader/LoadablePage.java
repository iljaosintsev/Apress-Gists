package com.turlir.abakgists.allgists.loader;

public class LoadablePage {

    public final int number, size;

    public LoadablePage(int start, int stop) {
        if (start >= stop) throw new IllegalArgumentException();
        size = stop - start;
        if (start % size != 0) throw new IllegalArgumentException();
        number = stop / size;
    }
}
