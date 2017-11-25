package com.turlir.abakgists.templater.base;

public class Group {

    public final int number;

    private int end; // номер последнего виджета в группе
    private int mStart;

    /**
     *
     * @param number позиция группы по порядку (с 0)
     * @param start номер первого элемента в группе (c 1)
     */
    public Group(int number, int start) {
        this.number = number;
        end = -1;
        mStart = start;
    }

    /**
     * Смена нумерации с 0
     */
    public void shift() {
        mStart--;
        end--;
    }

    /**
     * Установить последний элемент в группе
     * @param value номер с 1
     * @exception IllegalArgumentException если элементов в группе нет или она уже закрыта
     */
    public void close(int value) {
        if (value != -1 &&
            end == -1 && // не повторная установка
            value > mStart) {

            end = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean isClose() {
        return end == -1;
    }

    /**
     * Входит ли элемент в диапазон
     * @param index номер элемента
     * @return {@code true} если входит
     */
    public boolean does(int index) {
        return index <= end && index >= mStart;
    }

}
