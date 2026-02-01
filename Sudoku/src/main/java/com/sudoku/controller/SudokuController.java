package com.sudoku.controller;

import com.sudoku.dto.LoadPuzzleRequest;
import com.sudoku.dto.SaveSolutionRequest;
import com.sudoku.dto.SudokuResponse;
import com.sudoku.service.SudokuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sudoku")
public class SudokuController {

    private final SudokuService sudokuService;

    public SudokuController(SudokuService sudokuService) {
        this.sudokuService = sudokuService;
    }

    @PostMapping("/load")
    public ResponseEntity<SudokuResponse> load(@RequestBody LoadPuzzleRequest req) {
        return ResponseEntity.ok(sudokuService.load(req.getFilePath()));
    }

    @GetMapping("/board")
    public ResponseEntity<SudokuResponse> board() {
        return ResponseEntity.ok(sudokuService.getBoard());
    }

    @PostMapping("/solve")
    public ResponseEntity<SudokuResponse> solve() {
        return ResponseEntity.ok(sudokuService.solve());
    }

    @PostMapping("/save")
    public ResponseEntity<SudokuResponse> save(@RequestBody SaveSolutionRequest req) {
        return ResponseEntity.ok(sudokuService.save(req.getOutputPath()));
    }
}
