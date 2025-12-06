package com.acmebank.model;

public class CheckingAccount extends Account{

    public CheckingAccount(String accountNumber, double openingBalance, CardType cardType){
        super(accountNumber, AccountType.Checking, openingBalance, cardType);
    }

    @Override
    public void deposit(double amount) throws Exception{
        if (amount <= 0){
            throw new Exception("Deposit amount must be positive");
        }
        balance += amount;
    }

    @Override
    public void withdraw(double amount) throws Exception{
        if (!active){
            throw new Exception("Amount is deactivated");
        }
        if (amount <= 0){
            throw new Exception("Withdraw amount must be positive");
        }
        balance -= amount;

    }


}
