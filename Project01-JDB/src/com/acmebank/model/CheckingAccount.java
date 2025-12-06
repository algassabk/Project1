package com.acmebank.model;

public class CheckingAccount extends Account {

    public CheckingAccount(String accountNumber, double openingBalance, CardType cardType) {
        super(accountNumber, AccountType.Checking, openingBalance, cardType);
    }
}

