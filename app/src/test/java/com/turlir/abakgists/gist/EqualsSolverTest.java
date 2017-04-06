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
        boolean actual = mSolver.solveDesc(null, "text");
        assertTrue(actual);
    }

    @Test
    public void solveDescTextEmpty() {
        boolean actual = mSolver.solveDesc("text", "");
        assertTrue(actual);
    }

    @Test
    public void solveDescTextText() {
        boolean actual = mSolver.solveDesc("text", "text");
        assertFalse(actual);
    }

    @Test
    public void solveDescNullEmpty() {
        boolean actual = mSolver.solveDesc(null, "");
        assertFalse(actual);
    }

    @Test
    public void solveDescEmptyText() {
        boolean actual = mSolver.solveDesc("", "text");
        assertTrue(actual);
    }
    //</editor-fold>

    //<editor-fold desc="Note">
    @Test
    public void solveNoteNullText() throws Exception {
        boolean actual = mSolver.solveNote(null, "text");
        assertTrue(actual);
    }

    @Test
    public void solveNoteTextEmpty() {
        boolean actual = mSolver.solveNote("text", "");
        assertTrue(actual);
    }

    @Test
    public void solveNoteTextText() {
        boolean actual = mSolver.solveNote("text", "text");
        assertFalse(actual);
    }

    @Test
    public void solveNoteNullEmpty() {
        boolean actual = mSolver.solveNote(null, "");
        assertFalse(actual);
    }

    @Test
    public void solveNoteEmptyText() {
        boolean actual = mSolver.solveNote("", "text");
        assertTrue(actual);
    }
    //</editor-fold>

}