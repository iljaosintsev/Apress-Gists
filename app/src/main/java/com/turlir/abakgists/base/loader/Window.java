package com.turlir.abakgists.base.loader;

import com.turlir.abakgists.base.loader.server.LoadableItem;

public interface Window {

    int start();

    int stop();

    int addition();

    int count();

    Window next();

    Window prev();

    boolean hasNext();

    boolean hasPrevious();

    LoadableItem constraint(int count);
}
