package com.turlir.abakgists;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsSolverTest {

    private EqualsSolver solver;

    @Before
    public void setUp() {
        solver = new EqualsSolver();
    }

    //<editor-fold desc="Desc">
    @Test
    public void solveDescNullText() throws Exception {
        boolean actual = solver.solveDesc(null, "text");
        assertTrue(actual);
    }

    @Test
    public void solveDescTextEmpty() {
        boolean actual = solver.solveDesc("text", "");
        assertTrue(actual);
    }

    @Test
    public void solveDescTextText() {
        boolean actual = solver.solveDesc("text", "text");
        assertFalse(actual);
    }

    @Test
    public void solveDescNullEmpty() {
        boolean actual = solver.solveDesc(null, "");
        assertFalse(actual);
    }

    @Test
    public void solveDescEmptyText() {
        boolean actual = solver.solveDesc("", "text");
        assertTrue(actual);
    }
    //</editor-fold>

    //<editor-fold desc="Note">
    @Test
    public void solveNoteNullText() throws Exception {
        boolean actual = solver.solveNote(null, "text");
        assertTrue(actual);
    }

    @Test
    public void solveNoteTextEmpty() {
        boolean actual = solver.solveNote("text", "");
        assertTrue(actual);
    }

    @Test
    public void solveNoteTextText() {
        boolean actual = solver.solveNote("text", "text");
        assertFalse(actual);
    }

    @Test
    public void solveNoteNullEmpty() {
        boolean actual = solver.solveNote(null, "");
        assertFalse(actual);
    }

    @Test
    public void solveNoteEmptyText() {
        boolean actual = solver.solveNote("", "text");
        assertTrue(actual);
    }
    //</editor-fold>

}