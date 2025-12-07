package com.acmebank.model;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    protected String accountNumber;
    protected AccountType accountType;
    protected double balance;
    protected boolean active = true;
    protected int overdraftCount;
    protected CardType cardType;

    protected Account(String accountNumber, AccountType accountType,
                      double openingBalance, CardType cardType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = openingBalance;
        this.cardType = cardType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void deposit(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Deposit amount must be positive.");
        }

        balance += amount;

        // 🔁 Reactivate account if overdraft is cleared
        if (!active && balance >= 0 && overdraftCount >= 2) {
            active = true;
            overdraftCount = 0; // reset overdraft history
            System.out.println("Account has been reactivated after clearing overdraft balance.");
        }
    }

    public void withdraw(double amount) throws Exception {
        if (!active) {
            throw new Exception("Account is deactivated.");
        }
        if (amount <= 0) {
            throw new Exception("Withdraw amount must be positive.");
        }

        double newBalance = balance - amount;

        if (newBalance < 0) {
            // apply overdraft fee
            newBalance -= 35.0;
            overdraftCount++;

            if (newBalance < -100.0) {
                // do not allow going below -100
                throw new Exception("Cannot overdraw more than $100.");
            }

            if (overdraftCount >= 2) {
                active = false;
                System.out.println("Account has been deactivated due to repeated overdrafts.");
            }
        }

        balance = newBalance;
    }

    @Override
    public String toString() {
        return accountType + " #" + accountNumber + " (balance=" + balance + ")";
    }

    protected List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

}
