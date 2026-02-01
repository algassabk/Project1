package com.sudoku.handler;

import com.sudoku.exception.InvalidCharacterException;
import com.sudoku.exception.SudokuFileNotFoundException;
import com.sudoku.exception.SudokuUnsolvableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SudokuFileNotFoundException.class)
    public ResponseEntity<?> handleNotFound(SudokuFileNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidCharacterException.class)
    public ResponseEntity<?> handleInvalid(InvalidCharacterException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(SudokuUnsolvableException.class)
    public ResponseEntity<?> handleUnsolvable(SudokuUnsolvableException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    private ResponseEntity<?> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", message
                )
        );
    }
}
