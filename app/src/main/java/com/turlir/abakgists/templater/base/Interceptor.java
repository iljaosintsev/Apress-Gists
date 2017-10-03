package com.turlir.abakgists.templater.base;

import android.view.View;

public abstract class Interceptor<T extends View, V> {

    public abstract V bind();

    public void add(T view) {
        // stub
    }
}
