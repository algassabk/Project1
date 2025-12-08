package com.acmebank.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements StatementPrintable {

    protected String accountNumber;
    protected AccountType accountType;
    protected double balance;
    protected boolean active = true;
    protected int overdraftCount;
    protected CardType cardType;

    // daily limits tracking
    private LocalDate withdrawDay;
    private double withdrawTotalToday;

    private LocalDate transferDay;
    private double transferTotalTodayOther;
    private double transferTotalTodayOwn;

    // transaction history in memory
    protected List<Transaction> transactions = new ArrayList<>();

    protected Account(String accountNumber, AccountType accountType,
                      double openingBalance, CardType cardType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = openingBalance;
        this.cardType = cardType;
    }

    // ---------- BASIC GETTERS ----------
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

    // ---------- DEPOSIT / WITHDRAW WITH OVERDRAFT + REACTIVATION ----------
    public void deposit(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Deposit amount must be positive.");
        }

        balance += amount;

        // Reactivate account if overdraft is cleared
        if (!active && balance >= 0 && overdraftCount >= 2) {
            active = true;
            overdraftCount = 0; // reset overdraft history
            System.out.println("Account has been reactivated after clearing overdraft balance.");
        }
    }

    public void withdraw(double amount) throws Exception {
        if (!active) {
            throw new Exception("Account is deactivated.");
        }
        if (amount <= 0) {
            throw new Exception("Withdraw amount must be positive.");
        }

        // ---- DAILY WITHDRAW LIMIT ----
        LocalDate today = LocalDate.now();
        if (withdrawDay == null || !today.equals(withdrawDay)) {
            withdrawDay = today;
            withdrawTotalToday = 0;
        }

        double remainingLimit = cardType.getWithdrawLimitPerDay() - withdrawTotalToday;
        if (amount > remainingLimit) {
            throw new Exception("Daily withdraw limit exceeded. Remaining for today: " + remainingLimit);
        }

        // ---- OVERDRAFT LOGIC ----
        double newBalance = balance - amount;

        if (newBalance < 0) {
            // apply overdraft fee
            newBalance -= 35.0;
            overdraftCount++;

            if (newBalance < -100.0) {
                throw new Exception("Cannot overdraw more than $100.");
            }

            if (overdraftCount >= 2) {
                active = false;
                System.out.println("Account has been deactivated due to repeated overdrafts.");
            }
        }

        balance = newBalance;
        withdrawTotalToday += amount;
    }

    // ---------- TRANSFER LIMIT HELPERS ----------
    public void checkTransferLimit(double amount, boolean toOwnAccount) throws Exception {
        LocalDate today = LocalDate.now();

        if (transferDay == null || !transferDay.equals(today)) {
            transferDay = today;
            transferTotalTodayOther = 0;
            transferTotalTodayOwn = 0;
        }

        if (toOwnAccount) {
            double remaining = cardType.getTransferLimitOwnPerDay() - transferTotalTodayOwn;
            if (amount > remaining) {
                throw new Exception("Daily own-account transfer limit exceeded. Remaining: " + remaining);
            }
        } else {
            double remaining = cardType.getTransferLimitOtherPerDay() - transferTotalTodayOther;
            if (amount > remaining) {
                throw new Exception("Daily transfer-to-other limit exceeded. Remaining: " + remaining);
            }
        }
    }

    public void recordTransferAmount(double amount, boolean toOwnAccount) {
        if (toOwnAccount) {
            transferTotalTodayOwn += amount;
        } else {
            transferTotalTodayOther += amount;
        }
    }

    // ---------- TRANSACTION HISTORY ----------
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
    }

    // ---------- SUPPORT FOR FILE SAVE/LOAD ----------
    public int getOverdraftCount() {
        return overdraftCount;
    }

    public void setOverdraftCount(int overdraftCount) {
        this.overdraftCount = overdraftCount;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // ---------- DEBUG ----------
    @Override
    public String toString() {
        return accountType + " #" + accountNumber + " (balance=" + balance + ")";
    }
    @Override
    public void printStatement() {
        System.out.println("\n=== ACCOUNT STATEMENT (from Account.printStatement) ===");
        System.out.println("Account: " + accountType + " #" + accountNumber);
        System.out.printf("Current balance: %.2f%n", balance);

        // load all transactions from file
        var txs = TransactionFile.loadForAccount(accountNumber);
        if (txs.isEmpty()) {
            System.out.println("No transactions for this account yet.");
            return;
        }

        System.out.println("Date & Time           | Type         | Amount   | Post-balance | Description");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction tx : txs) {
            System.out.printf("%s | %-11s | %-8.2f | %-12.2f | %s%n",
                    tx.getDateTime(),
                    tx.getType(),
                    tx.getAmount(),
                    tx.getPostBalance(),
                    tx.getDescription());
        }
    }

}
