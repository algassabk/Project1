package com.acmebank.model;
import java.time.LocalDateTime;

public class Transaction {

    private final String transactionId;
    private final String accountNumber;
    private final LocalDateTime dateTime;
    private final TransactionType type;
    private final double amount;
    private final double postBalance;
    private final String note;

    public Transaction(String transactionId, String accountNumber,
                       LocalDateTime dateTime, TransactionType type,
                       double amount, double postBalance, String note) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.dateTime = dateTime;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.note = note;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
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

    public String getNote() {
        return note;
    }
    // later we will add toFileLine() and fromFileLine()
}
