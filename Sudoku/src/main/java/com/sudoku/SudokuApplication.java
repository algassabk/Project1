package com.sudoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SudokuApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SudokuApplication.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }
}
