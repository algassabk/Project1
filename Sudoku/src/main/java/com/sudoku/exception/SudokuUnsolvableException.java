package com.sudoku.exception;

public class SudokuUnsolvableException extends RuntimeException {
    public SudokuUnsolvableException(String message) {
        super(message);
    }
}
