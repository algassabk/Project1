package com.acmebank.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionFile {

    private static final Path TX_DIR = Paths.get("data", "Transactions");
    private static final DateTimeFormatter DT_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME; // 2025-12-10T12:34:56

    // -------- helpers --------
    private static void ensureDir() throws IOException {
        if (!Files.exists(TX_DIR)) {
            Files.createDirectories(TX_DIR);
        }
    }

    // One file per customer, e.g. Transactions-C001.txt
    private static Path fileForCustomer(String customerId) {
        String fileName = "Transactions-" + customerId + ".txt";
        return TX_DIR.resolve(fileName);
    }

    private static String nullToEmpty(String v) {
        return v == null ? "" : v;
    }

    private static String emptyToNull(String v) {
        return (v == null || v.isEmpty()) ? null : v;
    }

    // -------- APPEND ONE TRANSACTION --------
    public static void append(String customerId, Transaction tx) {
        try {
            ensureDir();
            Path file = fileForCustomer(customerId);

            try (BufferedWriter w = Files.newBufferedWriter(
                    file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                w.write(
                        tx.getTransactionId() + "|" +
                                nullToEmpty(tx.getFromAccountNumber()) + "|" +
                                nullToEmpty(tx.getToAccountNumber()) + "|" +
                                tx.getDateTime().format(DT_FORMAT) + "|" +
                                tx.getType().name() + "|" +
                                tx.getAmount() + "|" +
                                tx.getPostBalance() + "|" +
                                tx.getDescription()
                );
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing transaction file: " + e.getMessage());
        }
    }

    // -------- LOAD ALL TRANSACTIONS FOR A CUSTOMER --------
    public static List<Transaction> loadForCustomer(String customerId) {
        List<Transaction> result = new ArrayList<>();
        Path file = fileForCustomer(customerId);

        if (!Files.exists(file)) {
            return result; // no file yet
        }

        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                if (line.isBlank()) continue;

                String[] parts = line.split("\\|", 8);
                if (parts.length < 8) continue;

                String txId      = parts[0];
                String fromAcc   = emptyToNull(parts[1]);
                String toAcc     = emptyToNull(parts[2]);
                LocalDateTime dt = LocalDateTime.parse(parts[3], DT_FORMAT);
                TransactionType type = TransactionType.valueOf(parts[4]);
                double amount    = Double.parseDouble(parts[5]);
                double postBal   = Double.parseDouble(parts[6]);
                String desc      = parts[7];

                Transaction tx = new Transaction(
                        txId,
                        fromAcc,
                        toAcc,
                        dt,
                        type,
                        amount,
                        postBal,
                        desc
                );
                result.add(tx);
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Error reading transaction file for " + customerId + ": " + e.getMessage());
        }
        return result;
    }
}

