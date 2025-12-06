package com.acmebank.model;

public class Banker extends User{
    private String branchName;

    public Banker(String id, String name, String encryptedPassword, String branchName){
        super(id, name, Role.Banker, encryptedPassword);
        this.branchName = branchName;
    }

    public String getBranchName(){
        return branchName;
    }
}
