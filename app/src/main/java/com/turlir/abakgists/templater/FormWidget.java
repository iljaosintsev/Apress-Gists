package com.turlir.abakgists.templater;

public interface FormWidget<T> {

    void bind(T origin);

    T content();

    void showError(String error);

    // View view();

}
