package com.turlir.abakgists.allgists;

import com.turlir.abakgists.gistsloader.Accumulator;
import com.turlir.abakgists.base.loader.Window;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class AccumulatorTest {

    private Accumulator accum;

    @Test
    public void startWithZero() {
        accum = new Accumulator();
        assertEquals(0, accum.accumulator());
        assertEquals(0, accum.now(mockStart(0), 0));
    }

    @Test
    public void firstPart() {
        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(30, accum.accumulator());

        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(30), 30));
        assertEquals(30, accum.accumulator());
    }

    @Test
    public void continueUp() {
        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(60, accum.now(mockStart(30), 30));
        assertEquals(90, accum.now(mockStart(60), 30));

        assertEquals(90, accum.accumulator());
    }

    @Test
    public void rollbackLast() {
        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(60, accum.now(mockStart(30), 30));
        assertEquals(90, accum.now(mockStart(60), 30));
        assertEquals(90, accum.accumulator());

        assertEquals(60, accum.now(mockStart(30), 30));
        assertEquals(60, accum.accumulator());
    }

    @Test
    public void rollbackMultiply() {
        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(60, accum.now(mockStart(30), 30));
        assertEquals(90, accum.now(mockStart(60), 30));
        assertEquals(90, accum.accumulator());

        assertEquals(60, accum.now(mockStart(30), 30));
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(30, accum.accumulator());
    }

    @Test
    public void inPlace() {
        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(25, accum.now(mockStart(0), 25));
        assertEquals(25, accum.accumulator());

        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(60, accum.now(mockStart(30), 30));
        //assertEquals(59, accum.now(mockStart(30), 29));
        //assertEquals(58, accum.now(mockStart(30), 28));
        // etc..
        for (int i = 1; i < 30; i++) {
            assertEquals(60 - i, accum.now(mockStart(30), 30 - i));
        }
    }

    @Test
    public void lowStart() {
        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(30, accum.accumulator());
    }

    @Test
    public void overlap() {
        accum = new Accumulator();
        assertEquals(30, accum.now(mockStart(0), 30));
        assertEquals(30, accum.now(mockStart(15), 15));
    }

    private Window mockStart(int value, Integer... values) {
        Window w = Mockito.mock(Window.class);
        Mockito.when(w.start()).thenReturn(value, values);
        return w;
    }
}