package com.turlir.abakgists.base.erroring;

public interface ErrorInterpreter {

    /**
     * Не блокирующая ошибка, когда контент уже есть
     * @param msg описание ситуации
     */
    void nonBlockingError(String msg);

    /**
     * Случайная ошибка, когда определить дальнейшие действия нельзя. Например NPE
     * @param msg описание ситуации
     */
    void alertError(String msg);

    /**
     * Блокирующая, когда данных нет или дальнейшая работа невозможна
     * @param msg описание ситуации
     */
    void blockingError(String msg);
}
