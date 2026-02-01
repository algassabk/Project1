package com.sudoku.model;

public class SudokuCell {
    private int value;            // 0 means empty
    private final boolean fixed;  // true if it came from the puzzle

    public SudokuCell(int value, boolean fixed) {
        this.value = value;
        this.fixed = fixed;
    }

    public int getValue() {
        return value;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setValue(int value) {
        if (fixed) throw new IllegalStateException("Cannot modify a fixed cell.");
        this.value = value;
    }

    // solver uses this for backtracking
    public void forceSetValue(int value) {
        this.value = value;
    }
}
