package com.acmebank.model;

import java.time.LocalDateTime;

public class Transaction {

    private final String transactionId;
    private final String fromAccountNumber;   // can be null (for deposit)
    private final String toAccountNumber;     // can be null (for withdraw)
    private final LocalDateTime dateTime;
    private final TransactionType type;
    private final double amount;
    private final double postBalance;
    private final String description;

    public Transaction(String transactionId,
                       String fromAccountNumber,
                       String toAccountNumber,
                       LocalDateTime dateTime,
                       TransactionType type,
                       double amount,
                       double postBalance,
                       String description) {
        this.transactionId = transactionId;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.dateTime = dateTime;
        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.description = description;
    }

    public String getTransactionId()     { return transactionId; }
    public String getFromAccountNumber() { return fromAccountNumber; }
    public String getToAccountNumber()   { return toAccountNumber; }
    public LocalDateTime getDateTime()   { return dateTime; }
    public TransactionType getType()     { return type; }
    public double getAmount()            { return amount; }
    public double getPostBalance()       { return postBalance; }
    public String getDescription()       { return description; }
}
