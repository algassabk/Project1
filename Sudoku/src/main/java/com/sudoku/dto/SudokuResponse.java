package com.sudoku.dto;

public class SudokuResponse {
    private int[][] board;
    private String ascii;

    public SudokuResponse(int[][] board, String ascii) {
        this.board = board;
        this.ascii = ascii;
    }

    public int[][] getBoard() {
        return board;
    }

    public String getAscii() {
        return ascii;
    }
}
