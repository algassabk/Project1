package com.acmebank.model;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, double openingBalance, CardType cardType) {
        super(accountNumber, AccountType.Savings, openingBalance, cardType);
    }

    @Override
    public void deposit(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Deposit amount must be positive.");
        }
        balance += amount;
    }

    @Override
    public void withdraw(double amount) throws Exception {
        if (!active) {
            throw new Exception("Account is deactivated.");
        }
        if (amount <= 0) {
            throw new Exception("Withdraw amount must be positive.");
        }
        // overdraft logic will be implemented later
        balance -= amount;
    }
}
