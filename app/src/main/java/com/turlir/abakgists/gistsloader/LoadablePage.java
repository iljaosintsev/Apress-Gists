package com.turlir.abakgists.gistsloader;

import com.turlir.abakgists.base.loader.server.LoadableItem;

public class LoadablePage implements LoadableItem {

    public final int number, size;

    public LoadablePage(int start, int stop) {
        if (start >= stop) throw new IllegalArgumentException();
        size = stop - start;
        if (start % size != 0) throw new IllegalArgumentException();
        number = stop / size;
    }

    @Override
    public int firstAxis() {
        return number;
    }

    @Override
    public int secondAxis() {
        return size;
    }
}
