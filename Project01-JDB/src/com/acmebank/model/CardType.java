package com.acmebank.model;

public enum CardType implements Card {

    MastercardPlatinum(
            20_000, 40_000, 80_000,
            100_000, 200_000),
    MastercardTitanium(
            10_000, 20_000, 40_000,
            100_000, 200_000),
    Mastercard(
            5_000, 10_000, 20_000,
            100_000, 200_000);

    private final double withdrawLimitPerDay;
    private final double transferLimitPerDay;
    private final double transferLimitOwnPerDay;
    private final double depositLimitPerDay;
    private final double depositLimitOwnPerDay;

    CardType(double withdrawLimitPerDay,
             double transferLimitPerDay,
             double transferLimitOwnPerDay,
             double depositLimitPerDay,
             double depositLimitOwnPerDay) {
        this.withdrawLimitPerDay = withdrawLimitPerDay;
        this.transferLimitPerDay = transferLimitPerDay;
        this.transferLimitOwnPerDay = transferLimitOwnPerDay;
        this.depositLimitPerDay = depositLimitPerDay;
        this.depositLimitOwnPerDay = depositLimitOwnPerDay;
    }

    @Override
    public double getDailyWithdrawLimit() {
        return withdrawLimitPerDay;
    }

    @Override
    public double getDailyTransferLimitToOthers() {
        return transferLimitPerDay;
    }

    @Override
    public double getDailyTransferLimitOwn() {
        return transferLimitOwnPerDay;
    }

    @Override
    public double getDailyDepositLimit() {
        return depositLimitPerDay;
    }

    @Override
    public double getDailyDepositLimitOwn() {
        return depositLimitOwnPerDay;
    }

    @Override
    public String getDisplayName() {
        return switch (this) {
            case MastercardPlatinum -> "Mastercard Platinum";
            case MastercardTitanium -> "Mastercard Titanium";
            case Mastercard -> "Mastercard";
        };
    }
}
