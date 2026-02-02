package com.sudoku.model;

import com.sudoku.exception.InvalidCharacterException;
import com.sudoku.exception.SudokuFileNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sudoku {

    public static final int SIZE = 9;
    private final SudokuCell[][] board = new SudokuCell[SIZE][SIZE];

    public Sudoku() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                board[r][c] = new SudokuCell(0, false);
            }
        }
    }

    public SudokuCell[][] getBoard() {
        return board;
    }

    // reads file (supports '.' or '0' for empty), supports '|' and dashed separators
    public void loadFromFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new SudokuFileNotFoundException("Sudoku file not found: " + filePath);
        }

        List<Integer> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // ignore separator lines if present
                if (line.contains("---")) continue;

                // allow pipes in input (optional)
                line = line.replace("|", " ");

                String[] parts = line.split("\\s+");
                for (String p : parts) {
                    if (p.isBlank()) continue;

                    int v;

                    // '.' means empty cell
                    if (p.equals(".")) {
                        v = 0;
                    }
                    // allow '0' as empty cell too (optional)
                    else if (p.equals("0")) {
                        v = 0;
                    }
                    // allow digits 1–9
                    else if (p.matches("[1-9]")) {
                        v = Integer.parseInt(p);
                    } else {
                        throw new InvalidCharacterException(
                                "Invalid token '" + p + "' in file: " + filePath
                        );
                    }

                    // range check (extra safety)
                    if (v < 0 || v > 9) {
                        throw new InvalidCharacterException("Value out of range: " + v);
                    }

                    values.add(v);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }

        if (values.size() != 81) {
            throw new InvalidCharacterException("Expected 81 values but found " + values.size());
        }

        int idx = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int v = values.get(idx++);
                boolean fixed = (v != 0);
                board[r][c] = new SudokuCell(v, fixed);
            }
        }
    }

    // ✅ NEW: solution output with grids (numbers only, no dots)
    public String toSolutionAscii() {
        StringBuilder out = new StringBuilder();
        for (int r = 0; r < SIZE; r++) {
            if (r != 0 && r % 3 == 0) {
                out.append("----------------------------------\n\n");
            }
            for (int c = 0; c < SIZE; c++) {
                if (c != 0 && c % 3 == 0) out.append("|  ");
                int v = board[r][c].getValue();
                out.append(v).append("  ");
            }
            out.append("\n");
        }
        return out.toString();
    }

    // ✅ UPDATED: save solution using grid format
    public void saveSolutionToFile(String outputPath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            pw.print(toSolutionAscii());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + outputPath, e);
        }
    }

    public void forceSetValue(int row, int col, int value) {
        board[row][col].forceSetValue(value);
    }

    public int[][] toIntMatrix() {
        int[][] m = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                m[r][c] = board[r][c].getValue();
            }
        }
        return m;
    }

    // current board as ASCII (shows '.' for empty)
    public String toAscii() {
        StringBuilder out = new StringBuilder();
        for (int r = 0; r < SIZE; r++) {
            if (r != 0 && r % 3 == 0) {
                out.append("----------------------------------\n\n");
            }
            for (int c = 0; c < SIZE; c++) {
                if (c != 0 && c % 3 == 0) out.append(" | ");
                int v = board[r][c].getValue();
                out.append(v == 0 ? "." : v).append("  ");
            }
            out.append("\n");
        }
        return out.toString();
    }
}
