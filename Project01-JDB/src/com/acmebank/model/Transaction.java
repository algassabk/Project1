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

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getPostBalance() {
        return postBalance;
    }

    public String getDescription() {
        return description;
    }
}
