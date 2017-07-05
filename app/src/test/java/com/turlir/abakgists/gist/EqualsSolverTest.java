package com.turlir.abakgists.gist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsSolverTest {

    private EqualsSolver mSolver;

    @Before
    public void setUp() {
        mSolver = new EqualsSolver();
    }

    //<editor-fold desc="Desc">
    @Test
    public void solveDescNullText() throws Exception {
        boolean actual = mSolver.solveOldNow(null, "text");
        assertTrue(actual);
    }

    @Test
    public void solveDescTextEmpty() {
        boolean actual = mSolver.solveOldNow("text", "");
        assertTrue(actual);
    }

    @Test
    public void solveDescTextText() {
        boolean actual = mSolver.solveOldNow("text", "text");
        assertFalse(actual);
    }

    @Test
    public void solveDescNullEmpty() {
        boolean actual = mSolver.solveOldNow(null, "");
        assertFalse(actual);
    }

    @Test
    public void solveDescEmptyText() {
        boolean actual = mSolver.solveOldNow("", "text");
        assertTrue(actual);
    }
    //</editor-fold>

}