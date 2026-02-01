package com.sudoku.solver;

import com.sudoku.model.Sudoku;
import com.sudoku.model.SudokuCell;

public class SudokuSolver {

    public boolean solve(Sudoku sudoku) {
        SudokuCell[][] b = sudoku.getBoard();

        for (int r = 0; r < Sudoku.SIZE; r++) {
            for (int c = 0; c < Sudoku.SIZE; c++) {
                if (b[r][c].getValue() == 0) {

                    for (int val = 1; val <= 9; val++) {
                        if (isValid(b, r, c, val)) {
                            sudoku.forceSetValue(r, c, val);

                            if (solve(sudoku)) return true;

                            sudoku.forceSetValue(r, c, 0);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(SudokuCell[][] b, int row, int col, int val) {
        // row
        for (int c = 0; c < Sudoku.SIZE; c++) {
            if (b[row][c].getValue() == val) return false;
        }

        // col
        for (int r = 0; r < Sudoku.SIZE; r++) {
            if (b[r][col].getValue() == val) return false;
        }

        // box
        int sr = (row / 3) * 3;
        int sc = (col / 3) * 3;
        for (int r = sr; r < sr + 3; r++) {
            for (int c = sc; c < sc + 3; c++) {
                if (b[r][c].getValue() == val) return false;
            }
        }
        return true;
    }
}
