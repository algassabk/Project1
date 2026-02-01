package com.sudoku.service;

import com.sudoku.dto.SudokuResponse;
import com.sudoku.exception.SudokuUnsolvableException;
import com.sudoku.model.Sudoku;
import com.sudoku.solver.SudokuSolver;
import org.springframework.stereotype.Service;

@Service
public class SudokuServiceImpl implements SudokuService {

    private Sudoku currentSudoku = new Sudoku();
    private final SudokuSolver solver = new SudokuSolver();

    @Override
    public SudokuResponse load(String filePath) {
        currentSudoku = new Sudoku();
        currentSudoku.loadFromFile(filePath);
        return new SudokuResponse(currentSudoku.toIntMatrix(), currentSudoku.toAscii());
    }

    @Override
    public SudokuResponse getBoard() {
        return new SudokuResponse(currentSudoku.toIntMatrix(), currentSudoku.toAscii());
    }

    @Override
    public SudokuResponse solve() {
        boolean solved = solver.solve(currentSudoku);
        if (!solved) throw new SudokuUnsolvableException("This puzzle has no solution.");
        return new SudokuResponse(currentSudoku.toIntMatrix(), currentSudoku.toAscii());
    }

    @Override
    public SudokuResponse save(String outputPath) {
        currentSudoku.saveSolutionToFile(outputPath);
        return new SudokuResponse(currentSudoku.toIntMatrix(), "Saved to: " + outputPath);
    }
}
