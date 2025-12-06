package com.acmebank.model;

public abstract class Account {

    protected String accountNumber;
    protected AccountType accountType;
    protected double balance;
    protected boolean active = true;
    protected int overdraftCount;
    protected CardType cardType;

    protected Account(String accountNumber, AccountType accountType, double openingBalance, CardType cardType){
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

    public abstract void deposit(double amount) throws Exception;
    public abstract void withdraw(double amount) throws Exception;

    @Override
    public String toString(){
        return accountType + " #" + accountNumber + "(balance = " + balance + ")";
    }

}

