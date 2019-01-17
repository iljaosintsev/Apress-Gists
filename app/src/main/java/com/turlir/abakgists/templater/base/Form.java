package com.turlir.abakgists.templater.base;

import androidx.annotation.NonNull;

public interface Form<T> {

    void create();

    void connect();

    void bind(@NonNull T value);

    boolean verify();

    @NonNull
    T collect();

    ////

    void showError(String tag, String message);

    void enabled(String tag, boolean state);

    void enabledAll(boolean state);

    void visibility(String tag, int visibility);

}
