package com.acmebank.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User{
    private final List <Account> accounts = new ArrayList<>();

    public Customer(String id, String name, String encryptedPassword){
        super(id, name, Role.Customer, encryptedPassword);
    }

    public List<Account> getAccounts(){
        return accounts;
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public Account findAccountByNumber(String accountNumber){
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }
}
