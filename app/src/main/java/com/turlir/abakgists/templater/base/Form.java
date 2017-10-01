package com.turlir.abakgists.templater.base;

import android.support.annotation.NonNull;

interface Form<T> {

    void create();

    void connect();

    void bind(@NonNull T value);

    boolean verify();

    @NonNull T collect();

}
