package com.acmebank.model;

public interface Card {
    double getDailyWithdrawLimit();
    double getDailyTransferLimitToOthers();
    double getDailyTransferLimitOwn();
    double getDailyDepositLimit();
    double getDailyDepositLimitOwn();
    String getDisplayName();
}
