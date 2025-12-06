package com.acmebank.model;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, double openingBalance, CardType cardType) {
        super(accountNumber, AccountType.Savings, openingBalance, cardType);
    }
}
