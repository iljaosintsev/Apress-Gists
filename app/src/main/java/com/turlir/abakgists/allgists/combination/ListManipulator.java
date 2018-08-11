package com.turlir.abakgists.allgists.combination;

import java.util.List;

public interface ListManipulator<T> {

    void blockingLoad(boolean visible);

    void inlineLoad(boolean visible);

    void renderData(List<T> items);

    void emptyData(boolean visible);

    ErrorProcessor getErrorProcessor();
}
