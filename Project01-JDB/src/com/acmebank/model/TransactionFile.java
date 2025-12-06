package com.acmebank.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionFile {

    // Folder: data/transactions/
    private static final Path BASE_DIR = Paths.get("data", "Transactions");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static Path getFileForCustomer(Customer customer) {
        String fileName = "Transactions-" + customer.getId() + ".txt";
        return BASE_DIR.resolve(fileName);
    }

    public static void appendTransaction(Customer customer, Transaction tx) {
        try {
            if (!Files.exists(BASE_DIR)) {
                Files.createDirectories(BASE_DIR);
            }

            Path file = getFileForCustomer(customer);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    file, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

                // simple pipe-separated line
                String line = String.join("|",
                        tx.getTransactionId(),
                        tx.getDateTime().format(FORMATTER),
                        tx.getFromAccount() == null ? "" : tx.getFromAccount(),
                        tx.getToAccount() == null ? "" : tx.getToAccount(),
                        tx.getType().name(),
                        String.valueOf(tx.getAmount()),
                        String.valueOf(tx.getPostBalance()),
                        tx.getDescription()
                );
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Could not write transaction file: " + e.getMessage());
        }
    }

    // Optional: read all lines (for advanced filtering later)
    public static List<String> readRawLines(Customer customer) {
        Path file = getFileForCustomer(customer);
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            System.out.println("Could not read transaction file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
