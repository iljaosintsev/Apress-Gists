package com.turlir.abakgists.allgists.loader;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WindowDifferTest {

    private WindowDiffer differ;

    @Before
    public void setup() {
        differ = new WindowDiffer();
    }

    // diff

    @Test
    public void aliquotDiff() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 30);
        Window diff = differ.diff(demand, actual);
        assertTrue(new Range(30, 45, 15).equals(diff));
    }

    @Test
    public void incompleteDiff() {
        Range demand = new Range(15, 45, 15);
        Range actual = new Range(15, 35, 15);
        Window diff = differ.diff(demand, actual);
        assertTrue(new Range(30, 45, 15).equals(diff));

        demand = new Range(15, 45, 15);
        actual = new Range(15, 37, 15);
        diff = differ.diff(demand, actual);
        assertTrue(new Range(30, 45, 15).equals(diff));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invertDiff() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 15 + 15);
        differ.diff(actual, demand);
    }

    @Test(expected = IllegalArgumentException.class)
    public void diffWithEqualsRange() {
        Range demand = new Range(15, 45);
        Range actual = new Range(15, 45);
        differ.diff(actual, demand);
    }

    // cut

    @Test
    public void correctCut() {
        Range range = new Range(15, 45);
        Window actual = differ.cut(range, 15);
        check(actual, 15, 30);

        actual = differ.cut(range, 1);
        check(actual, 15, 16);

        actual = differ.cut(range, 30);
        check(actual, 15, 45);
    }

    @Test
    public void cutBeforeStart() {
        Range range = new Range(30, 60);
        Window actual = differ.cut(range, 15);
        check(actual, 30, 45);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cutOutOfRange() {
        Range range = new Range(15, 45);
        differ.cut(range, 31);
    }

    private void check(Window r, int s, int e) {
        assertEquals(new Range(s, e), r);
    }

}