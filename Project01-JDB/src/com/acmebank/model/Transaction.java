package com.acmebank.model;

import java.time.LocalDateTime;

public class Transaction {

    private final String transactionId;
    private final String fromAccount;
    private final String toAccount;
    private final LocalDateTime dateTime;
    private final TransactionType type;
    private final double amount;
    private final double postBalance;
    private final String description;

    public Transaction(String transactionId,
                       String fromAccount,
                       String toAccount,
                       LocalDateTime dateTime,
                       TransactionType type,
                       double amount,
                       double postBalance,
                       String description) {

        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.dateTime = dateTime;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.description = description;
    }

    public String getTransactionId() { return transactionId; }
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public LocalDateTime getDateTime() { return dateTime; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public double getPostBalance() { return postBalance; }
    public String getDescription() { return description; }

    // ---------- FILE SAVE FORMAT ----------
    public String toFileLine() {
        return transactionId + "|" +
                (fromAccount == null ? "" : fromAccount) + "|" +
                (toAccount == null ? "" : toAccount) + "|" +
                dateTime.toString() + "|" +
                type.name() + "|" +
                amount + "|" +
                postBalance + "|" +
                description.replace("|", "/");
    }

    public static Transaction fromFileLine(String line) {
        String[] parts = line.split("\\|", 8);
        if (parts.length < 8) return null;

        String id = parts[0];
        String from = parts[1].isEmpty() ? null : parts[1];
        String to = parts[2].isEmpty() ? null : parts[2];
        LocalDateTime dt = LocalDateTime.parse(parts[3]);
        TransactionType type = TransactionType.valueOf(parts[4]);
        double amount = Double.parseDouble(parts[5]);
        double postBal = Double.parseDouble(parts[6]);
        String desc = parts[7];

        return new Transaction(id, from, to, dt, type, amount, postBal, desc);
    }
}
