package com.sudoku.service;

import com.sudoku.dto.SudokuResponse;

public interface SudokuService {
    SudokuResponse load(String filePath);
    SudokuResponse getBoard();
    SudokuResponse solve();
    SudokuResponse save(String outputPath);
}
