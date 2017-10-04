package com.turlir.abakgists.templater.base;

public interface FormWidget<T> {

    void bind(T origin);

    T content();

    void showError(String error);

}
