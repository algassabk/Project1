package com.acmebank.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionFile {

    private static final Path TX_DIR = Paths.get("Transactions");

    private static void ensureDir() throws IOException {
        if (!Files.exists(TX_DIR)) {
            Files.createDirectories(TX_DIR);
        }
    }

    private static Path fileForAccount(String accountNumber) {
        String fileName = "Account-" + accountNumber + ".txt";
        return TX_DIR.resolve(fileName);
    }

    //save 1 transaction
    public static void append(String accountNumber, Transaction tx) {
        try {
            ensureDir();
            Path file = fileForAccount(accountNumber);
            try (BufferedWriter w = Files.newBufferedWriter(
                    file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                w.write(tx.toFileLine());
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    // load all transactions to an account
    public static List<Transaction> loadForAccount(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        Path file = fileForAccount(accountNumber);

        if (!Files.exists(file)) {
            return result;
        }

        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                Transaction tx = Transaction.fromFileLine(line);
                if (tx != null) {
                    result.add(tx);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }

        return result;
    }
}
