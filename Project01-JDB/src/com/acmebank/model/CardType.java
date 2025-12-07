package com.acmebank.model;

public enum CardType {

    MastercardPlatinum(
            "Mastercard Platinum",
            20000,   // withdrawLimitPerDay
            40000,   // transferLimitOtherPerDay
            80000,   // transferLimitOwnPerDay
            100000,  // depositLimitPerDay
            200000   // depositLimitOwnPerDay
    ),
    MastercardTitanium(
            "Mastercard Titanium",
            10000,
            20000,
            40000,
            100000,
            200000
    ),
    Mastercard(
            "Mastercard",
            5000,
            10000,
            20000,
            100000,
            200000
    );

    private final String displayName;
    private final double withdrawLimitPerDay;
    private final double transferLimitOtherPerDay;
    private final double transferLimitOwnPerDay;
    private final double depositLimitPerDay;
    private final double depositLimitOwnPerDay;

    CardType(String displayName,
             double withdrawLimitPerDay,
             double transferLimitOtherPerDay,
             double transferLimitOwnPerDay,
             double depositLimitPerDay,
             double depositLimitOwnPerDay) {
        this.displayName = displayName;
        this.withdrawLimitPerDay = withdrawLimitPerDay;
        this.transferLimitOtherPerDay = transferLimitOtherPerDay;
        this.transferLimitOwnPerDay = transferLimitOwnPerDay;
        this.depositLimitPerDay = depositLimitPerDay;
        this.depositLimitOwnPerDay = depositLimitOwnPerDay;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getWithdrawLimitPerDay() {
        return withdrawLimitPerDay;
    }

    public double getTransferLimitOtherPerDay() {
        return transferLimitOtherPerDay;
    }

    public double getTransferLimitOwnPerDay() {
        return transferLimitOwnPerDay;
    }

    public double getDepositLimitPerDay() {
        return depositLimitPerDay;
    }

    public double getDepositLimitOwnPerDay() {
        return depositLimitOwnPerDay;
    }
}

