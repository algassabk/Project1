package com.acmebank.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class AccountFile {

    private static final Path ACCOUNTS_DIR = Paths.get("data", "accounts");

    private static void ensureDir() throws IOException {
        if (!Files.exists(ACCOUNTS_DIR)) {
            Files.createDirectories(ACCOUNTS_DIR);
        }
    }

    private static Path fileForAccount(Customer owner, Account acc) {
        String fileName = "Account-" + owner.getId() + "-" + acc.getAccountNumber() + ".txt";
        return ACCOUNTS_DIR.resolve(fileName);
    }

    public static void saveAccountsForCustomer(Customer customer) {
        try {
            ensureDir();
            for (Account acc : customer.getAccounts()) {
                Path file = fileForAccount(customer, acc);
                try (BufferedWriter w = Files.newBufferedWriter(
                        file,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {

                    w.write("customerId=" + customer.getId());          w.newLine();
                    w.write("accountNumber=" + acc.getAccountNumber()); w.newLine();
                    w.write("accountType=" + acc.getAccountType());     w.newLine();
                    w.write("balance=" + acc.getBalance());             w.newLine();
                    w.write("status=" + (acc.isActive() ? "ACTIVE" : "INACTIVE")); w.newLine();
                    w.write("cardType=" + acc.getCardType().name());   w.newLine();
                    w.write("overdraftCount=" + acc.getOverdraftCount()); w.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    public static void loadAccounts(List<User> users) {
        if (!Files.exists(ACCOUNTS_DIR)) {
            return; // first run, no accounts yet
        }

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(ACCOUNTS_DIR, "Account-*.txt")) {

            for (Path file : stream) {
                List<String> lines = Files.readAllLines(file);

                String customerId = null;
                String accountNumber = null;
                String accountTypeStr = null;
                String balanceStr = null;
                String statusStr = null;
                String cardTypeStr = null;
                String overdraftStr = null;

                for (String line : lines) {
                    String[] parts = line.split("=", 2);
                    if (parts.length != 2) continue;

                    switch (parts[0]) {
                        case "customerId"     -> customerId = parts[1];
                        case "accountNumber"  -> accountNumber = parts[1];
                        case "accountType"    -> accountTypeStr = parts[1];
                        case "balance"        -> balanceStr = parts[1];
                        case "status"         -> statusStr = parts[1];
                        case "cardType"       -> cardTypeStr = parts[1];
                        case "overdraftCount" -> overdraftStr = parts[1];
                    }
                }

                if (customerId == null || accountNumber == null ||
                        accountTypeStr == null || balanceStr == null || cardTypeStr == null) {
                    continue;
                }

                // find owner
                final String cid = customerId;

                Customer owner = users.stream()
                        .filter(u -> u instanceof Customer && u.getId().equals(cid))
                        .map(u -> (Customer) u)
                        .findFirst()
                        .orElse(null);


                if (owner == null) continue;

                double balance = Double.parseDouble(balanceStr);
                AccountType accountType = AccountType.valueOf(accountTypeStr);
                CardType cardType = CardType.valueOf(cardTypeStr);

                Account acc;
                if (accountType == AccountType.Checking) {
                    acc = new CheckingAccount(accountNumber, balance, cardType);
                } else {
                    acc = new SavingsAccount(accountNumber, balance, cardType);
                }

                if ("INACTIVE".equalsIgnoreCase(statusStr)) {
                    acc.setActive(false);
                }
                if (overdraftStr != null) {
                    acc.setOverdraftCount(Integer.parseInt(overdraftStr));
                }

                owner.addAccount(acc);
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
}

