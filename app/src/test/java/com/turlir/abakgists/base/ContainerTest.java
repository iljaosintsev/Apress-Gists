package com.turlir.abakgists.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerTest {

    @Test
    public void applyTest() {
        Container<Integer> c = new Container<>(2)
                .operate(value -> value + 3);
        assertEquals(new Integer(5), c.getValue());
    }

    @Test
    public void typeTest() {
        Container<String> b = new Container<>(2)
                .type(value -> new Container<>(value.toString()));
        assertEquals("2", b.getValue());
    }

    @Test
    public void alternateGetTest() {
       String a = new Container<String>(null)
                .alternateGet("safe");
       assertEquals("safe", a);
    }

    @Test
    public void npeOperateTest() {
        String a = new Container<String>(null)
                .npeOperate(value -> value.length() + "ops")
                .getValue();
        assertTrue(null == a);
    }

    @Test
    public void combinationTest() {
        String a = new Container<String>(null)
                .npeOperate(value -> value.length() + "ops")
                .alternateGet("safe");
        assertEquals("safe", a);
    }

    static class Container<T> {

        private T mValue;

        Container(T value) {
            mValue = value;
        }

        Container<T> operate(Operator<T> op) {
            mValue = op.operate(mValue);
            return this;
        }

        <Q> Container<Q> type(Typing<T, Q> op) {
            return op.operate(mValue);
        }

        T getValue() {
            return mValue;
        }

        T alternateGet(T alternative) {
            if (mValue != null) {
                return mValue;
            } else {
                return alternative;
            }
        }

        Container<T> npeOperate(Operator<T> op) {
            try {
                mValue = op.operate(mValue);
                return this;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return new Container<>(null);
            }
        }

    }

    private interface Operator<T> {

        T operate(T value);
    }

    private interface Typing<T, Q> {

        Container<Q> operate(T value);
    }
}
