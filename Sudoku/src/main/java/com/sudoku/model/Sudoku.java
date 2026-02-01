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

    // reads file of 81 numbers (0-9) separated by spaces
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

                    if (!p.matches("[0-9]")) {
                        throw new InvalidCharacterException("Invalid token '" + p + "' in file: " + filePath);
                    }

                    int v = Integer.parseInt(p);
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

    public void saveSolutionToFile(String outputPath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            for (int r = 0; r < SIZE; r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < SIZE; c++) {
                    sb.append(board[r][c].getValue());
                    if (c < SIZE - 1) sb.append(" ");
                }
                pw.println(sb);
            }
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

    public String toAscii() {
        StringBuilder out = new StringBuilder();
        for (int r = 0; r < SIZE; r++) {
            if (r != 0 && r % 3 == 0) {
                out.append("----------------------------------\n\n");
            }
            for (int c = 0; c < SIZE; c++) {
                if (c != 0 && c % 3 == 0) out.append(" | ");
                out.append(board[r][c].getValue()).append("  ");
            }
            out.append("\n");
        }
        return out.toString();
    }
}
