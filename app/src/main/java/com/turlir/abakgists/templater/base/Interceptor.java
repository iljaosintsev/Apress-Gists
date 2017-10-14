package com.turlir.abakgists.templater.base;

import android.view.View;

public abstract class Interceptor<T extends View> {

    public abstract String bind();

    public void add(T view) {
        // stub
    }
}
