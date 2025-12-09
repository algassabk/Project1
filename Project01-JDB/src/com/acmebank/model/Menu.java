package com.acmebank.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final List<User> users;   // shared list of all users (bankers + customers)

    public Menu(List<User> users) {
        this.users = users;
    }
    private static final DateTimeFormatter TX_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public void showMainMenu(User user) {
        if (user instanceof Banker banker) {
            showBankerMenu(banker);
        } else if (user instanceof Customer customer) {
            showCustomerMenu(customer);
        }
    }

    // BANKER MENU
    private void showBankerMenu(Banker banker) {
        while (true) {
            System.out.println("\n--- BANKER MENU ---");
            System.out.println("1. Add new customer");
            System.out.println("2. View all customers");
            System.out.println("3. Logout");
            System.out.print("Choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> addNewCustomer();
                case 2 -> listCustomers();
                case 3 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void addNewCustomer() {
        System.out.println("\n=== Add New Customer ===");

        System.out.print("Enter new customer ID (e.g., C002): ");
        String id = scanner.nextLine();

        // check if ID already exists
        boolean exists = users.stream().anyMatch(u -> u.getId().equals(id));
        if (exists) {
            System.out.println("A user with this ID already exists.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Set password for customer: ");
        String plainPassword = scanner.nextLine();
        String hash = PasswordHelper.hash(plainPassword);

        Customer newCustomer = new Customer(id, name, hash);

        // choose account
        System.out.println("Select account option:");
        System.out.println("1. Checking only");
        System.out.println("2. Savings only");
        System.out.println("3. Both checking and savings");
        System.out.print("Choice: ");
        int accChoice = readInt();

        List<Account> createdAccounts = new ArrayList<>();

        if (accChoice == 1 || accChoice == 3) {
            Account checking = createAccount(AccountType.Checking);
            newCustomer.addAccount(checking);
            createdAccounts.add(checking);
        }

        if (accChoice == 2 || accChoice == 3) {
            Account savings = createAccount(AccountType.Savings);
            newCustomer.addAccount(savings);
            createdAccounts.add(savings);
        }

        users.add(newCustomer);
        UserFile.saveCustomer(newCustomer);
        AccountFile.saveAccountsForCustomer(newCustomer);


        System.out.println("\nCustomer created successfully!");
        System.out.println("Customer ID: " + newCustomer.getId() + ", Name: " + newCustomer.getName());
        System.out.println("Accounts:");
        for (Account acc : createdAccounts) {
            System.out.println(" - " + acc.getAccountType() + " #" + acc.getAccountNumber()
                    + " | Balance: " + acc.getBalance()
                    + " | Card: " + acc.getCardType().getDisplayName());
        }
    }

    private Account createAccount(AccountType type) {
        System.out.println("\nCreating " + type + " account...");

        System.out.print("Enter opening balance: ");
        double openingBalance = readDouble();

        System.out.println("Choose card type:");
        System.out.println("1. Mastercard Platinum");
        System.out.println("2. Mastercard Titanium");
        System.out.println("3. Mastercard");
        System.out.print("Choice: ");
        int cardChoice = readInt();

        CardType cardType = switch (cardChoice) {
            case 1 -> CardType.MastercardPlatinum;
            case 2 -> CardType.MastercardTitanium;
            default -> CardType.Mastercard;
        };

        String accountNumber = generateAccountNumber();

        if (type == AccountType.Checking) {
            return new CheckingAccount(accountNumber, openingBalance, cardType);
        } else {
            return new SavingsAccount(accountNumber, openingBalance, cardType);
        }
    }

    private void listCustomers() {
        System.out.println("\n=== All Customers ===");
        users.stream()
                .filter(u -> u instanceof Customer)
                .map(u -> (Customer) u)
                .forEach(c -> System.out.println(c.getId() + " - " + c.getName()));
    }

    // CUSTOMER MENU
    private void showCustomerMenu(Customer customer) {
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. View balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View transactions");
            System.out.println("6. Account statement");
            System.out.println("7. Logout");
            System.out.print("Choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> viewBalance(customer);
                case 2 -> depositToAccount(customer);
                case 3 -> withdrawFromAccount(customer);
                case 4 -> transferMoney(customer);
                case 5 -> showTransactions(customer);
                case 6 -> showAccountStatement(customer);   //  new
                case 7 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
    private void showAccountStatement(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;

        // Load ALL transactions for this customer
        List<Transaction> all = TransactionFile.loadForCustomer(customer.getId());

        // Filter only transactions belonging to this account
        List<Transaction> txs = new ArrayList<>();
        String accNo = account.getAccountNumber();

        for (Transaction tx : all) {
            String from = tx.getFromAccountNumber();
            String to   = tx.getToAccountNumber();

            if ((from != null && from.equals(accNo)) ||
                    (to != null && to.equals(accNo))) {
                txs.add(tx);
            }
        }

        System.out.println("\n=== ACCOUNT STATEMENT ===");
        System.out.println("Account: " + account.getAccountType() + " #" + account.getAccountNumber());
        System.out.printf("Current balance: %.2f%n", account.getBalance());

        if (txs.isEmpty()) {
            System.out.println("No transactions for this account yet.");
            return;
        }

        // reuse the common printer
        printTransactions(txs);
    }



    //CUSTOMER ACTIONS

    private Account selectAccount(Customer customer) {
        List<Account> accounts = customer.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("You have no accounts.");
            return null;
        }
        if (accounts.size() == 1) {
            return accounts.get(0);
        }

        System.out.println("\nSelect account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.println((i + 1) + ". " + acc.getAccountType() +
                    " #" + acc.getAccountNumber() +
                    " | Balance: " + acc.getBalance());
        }
        System.out.print("Choice: ");
        int choice = readInt();

        if (choice < 1 || choice > accounts.size()) {
            System.out.println("Invalid account choice.");
            return null;
        }
        return accounts.get(choice - 1);
    }

    private void viewBalance(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;

        System.out.println("\nAccount: " + account.getAccountType() +
                " #" + account.getAccountNumber());
        System.out.println("Current balance: " + account.getBalance());
        System.out.println("Status: " + (account.isActive() ? "Active" : "Deactivated"));
    }

    private void depositToAccount(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;

        System.out.print("Enter amount to deposit: ");
        double amount = readDouble();

        try {
            account.deposit(amount);

            Transaction tx = new Transaction(
                    generateTransactionId(), null, account.getAccountNumber(),
                    LocalDateTime.now(), TransactionType.Deposit, amount, account.getBalance(),
                    "Deposit"
            );
            account.addTransaction(tx);

            // save to customer's transaction file
            TransactionFile.append(customer.getId(), tx);

            // save account state
            AccountFile.saveAccountsForCustomer(customer);

            System.out.println("Deposit successful. New balance: " + account.getBalance());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void withdrawFromAccount(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;

        System.out.print("Enter amount to withdraw: ");
        double amount = readDouble();

        try {
            account.withdraw(amount);

            Transaction tx = new Transaction(
                    generateTransactionId(), account.getAccountNumber(), null, LocalDateTime.now(),
                    TransactionType.Withdraw, amount, account.getBalance(), "Withdraw"
            );
            account.addTransaction(tx);

            TransactionFile.append(customer.getId(), tx);
            AccountFile.saveAccountsForCustomer(customer);

            System.out.println("Withdraw successful. New balance: " + account.getBalance());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void transferMoney(Customer customer) {
        System.out.println("\n=== Transfer Money ===");
        Account fromAccount = selectAccount(customer);
        if (fromAccount == null) return;

        System.out.println("Transfer to:");
        System.out.println("1. My other account");
        System.out.println("2. Another customer's account");
        System.out.print("Choice: ");
        int choice = readInt();

        Account toAccount = null;

        if (choice == 1) {
            toAccount = selectOtherAccountOfSameCustomer(customer, fromAccount);
        } else if (choice == 2) {
            toAccount = selectAccountOfAnotherCustomer();
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        if (toAccount == null) return;

        System.out.print("Enter amount to transfer: ");
        double amount = readDouble();

        try {
            // if transfer must  own account
            boolean isOwnAccount = customer.getAccounts().contains(toAccount);
            // check daily limits
            fromAccount.checkTransferLimit(amount, isOwnAccount);

            fromAccount.withdraw(amount);
            toAccount.deposit(amount);

            fromAccount.recordTransferAmount(amount, isOwnAccount);



            recordTransfer(fromAccount, toAccount, amount);

            System.out.println("Transfer successful.");
            System.out.println("New balance: " + toAccount.getBalance());

        } catch (Exception e) {
            System.out.println("Error during transfer: " + e.getMessage());
        }
    }

    private Account selectOtherAccountOfSameCustomer(Customer customer, Account fromAccount) {
        List<Account> accounts = customer.getAccounts();
        List<Account> others = new ArrayList<>();

        for (Account acc : accounts) {
            if (!acc.getAccountNumber().equals(fromAccount.getAccountNumber())) {
                others.add(acc);
            }
        }

        if (others.isEmpty()) {
            System.out.println("You have only one account. No other account to transfer to.");
            return null;
        }

        System.out.println("\nSelect destination account:");
        for (int i = 0; i < others.size(); i++) {
            Account acc = others.get(i);
            System.out.println((i + 1) + ". " + acc.getAccountType() +
                    " #" + acc.getAccountNumber() +
                    " | Balance: " + acc.getBalance());
        }
        System.out.print("Choice: ");
        int choice = readInt();

        if (choice < 1 || choice > others.size()) {
            System.out.println("Invalid choice.");
            return null;
        }
        return others.get(choice - 1);
    }

    private Account selectAccountOfAnotherCustomer() {
        System.out.print("Enter other customer's ID: ");
        String otherId = scanner.nextLine();

        Customer otherCustomer = users.stream()
                .filter(u -> u instanceof Customer && u.getId().equals(otherId))
                .map(u -> (Customer) u)
                .findFirst()
                .orElse(null);

        if (otherCustomer == null) {
            System.out.println("Customer not found.");
            return null;
        }

        return selectAccountWithoutBalance(otherCustomer);
    }

    // Use this when selecting an account that belongs to ANOTHER customer
    private Account selectAccountWithoutBalance(Customer customer) {
        List<Account> accounts = customer.getAccounts();
        if (accounts.isEmpty()) {
            System.out.println("This customer has no accounts.");
            return null;
        }
        if (accounts.size() == 1) {
            return accounts.get(0);
        }

        System.out.println("\nSelect account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.println((i + 1) + ". " + acc.getAccountType() +
                    " #" + acc.getAccountNumber());   //  no balance printed here
        }
        System.out.print("Choice: ");
        int choice = readInt();

        if (choice < 1 || choice > accounts.size()) {
            System.out.println("Invalid choice.");
            return null;
        }
        return accounts.get(choice - 1);
    }


    private void showTransactions(Customer customer) {
        Account account = selectAccount(customer);
        if (account == null) return;

        // Load all transactions for this customer
        List<Transaction> all = TransactionFile.loadForCustomer(customer.getId());
        String accNo = account.getAccountNumber();

        // Filter only this accounts transactions
        List<Transaction> accountTx = new ArrayList<>();
        for (Transaction tx : all) {
            String from = tx.getFromAccountNumber();
            String to   = tx.getToAccountNumber();

            if (accNo.equals(from) || accNo.equals(to)) {
                accountTx.add(tx);
            }
        }

        if (accountTx.isEmpty()) {
            System.out.println("No transactions for this account.");
            return;
        }

        while (true) {
            System.out.println("\n=== Transactions for " + account.getAccountNumber() + " ===");
            System.out.println("1. All");
            System.out.println("2. Today");
            System.out.println("3. Yesterday");
            System.out.println("4. Last 7 days");
            System.out.println("5. Last 30 days");
            System.out.println("6. Last month (calendar)");
            System.out.println("7. Back");
            System.out.print("Choice: ");

            int choice = readInt();
            if (choice == 7) return;

            LocalDate today = LocalDate.now();
            List<Transaction> filtered = new ArrayList<>();

            switch (choice) {
                case 1 -> filtered = accountTx;

                case 2 -> {
                    for (Transaction tx : accountTx) {
                        if (tx.getDateTime().toLocalDate().equals(today)) {
                            filtered.add(tx);
                        }
                    }
                }

                case 3 -> {
                    LocalDate yesterday = today.minusDays(1);
                    for (Transaction tx : accountTx) {
                        if (tx.getDateTime().toLocalDate().equals(yesterday)) {
                            filtered.add(tx);
                        }
                    }
                }

                case 4 -> {
                    LocalDate from = today.minusDays(7);
                    for (Transaction tx : accountTx) {
                        LocalDate d = tx.getDateTime().toLocalDate();
                        if (!d.isBefore(from)) {
                            filtered.add(tx);
                        }
                    }
                }

                case 5 -> {
                    LocalDate from = today.minusDays(30);
                    for (Transaction tx : accountTx) {
                        LocalDate d = tx.getDateTime().toLocalDate();
                        if (!d.isBefore(from)) {
                            filtered.add(tx);
                        }
                    }
                }

                case 6 -> {
                    LocalDate firstOfThisMonth = today.withDayOfMonth(1);
                    LocalDate firstOfLastMonth = firstOfThisMonth.minusMonths(1);
                    LocalDate lastOfLastMonth = firstOfThisMonth.minusDays(1);

                    for (Transaction tx : accountTx) {
                        LocalDate d = tx.getDateTime().toLocalDate();
                        if (!d.isBefore(firstOfLastMonth) && !d.isAfter(lastOfLastMonth)) {
                            filtered.add(tx);
                        }
                    }
                }

                default -> {
                    System.out.println("Invalid choice.");
                    continue;
                }
            }

            if (filtered.isEmpty()) {
                System.out.println("No transactions found for this period.");
            } else {
                printTransactions(filtered);
            }
        }
    }



    private void recordTransfer(Account fromAccount, Account toAccount, double amount) {
        LocalDateTime now = LocalDateTime.now();

        Transaction outTx = new Transaction(
                generateTransactionId(),
                fromAccount.getAccountNumber(),
                toAccount.getAccountNumber(),
                now,
                TransactionType.Transfer_out,
                amount,
                fromAccount.getBalance(),
                "Transfer to " + toAccount.getAccountNumber()
        );

        Transaction inTx = new Transaction(
                generateTransactionId(),
                fromAccount.getAccountNumber(),
                toAccount.getAccountNumber(),
                now,
                TransactionType.Transfer_in,
                amount,
                toAccount.getBalance(),
                "Transfer from " + fromAccount.getAccountNumber()
        );

        fromAccount.addTransaction(outTx);
        toAccount.addTransaction(inTx);

        Customer fromCustomer = findCustomerByAccount(fromAccount);
        Customer toCustomer   = findCustomerByAccount(toAccount);

        if (fromCustomer != null) {
            TransactionFile.append(fromCustomer.getId(), outTx);
            AccountFile.saveAccountsForCustomer(fromCustomer);
        }
        if (toCustomer != null && toCustomer != fromCustomer) {
            TransactionFile.append(toCustomer.getId(), inTx);
            AccountFile.saveAccountsForCustomer(toCustomer);
        }
    }




    //HELPER METHODS
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid amount: ");
            }
        }
    }

    private String generateAccountNumber() {
        int random = (int) (Math.random() * 900_000) + 100_000; // 6-digit number
        return "ACC" + random;
    }
    private String generateTransactionId() {
        int random = (int) (Math.random() * 900_000) + 100_000;
        return "TX" + random;
    }

    private Customer findCustomerByAccount(Account account) {
        return users.stream()
                .filter(u -> u instanceof Customer)
                .map(u -> (Customer) u)
                .filter(c -> c.getAccounts().contains(account))
                .findFirst()
                .orElse(null);
    }

    private void printTransactions(List<Transaction> txs) {
        System.out.println("\nDate & Time           | Type         | Amount   | Post-balance | Description");
        System.out.println("--------------------------------------------------------------------------");
        for (Transaction tx : txs) {
            String dt = TX_FORMATTER.format(tx.getDateTime());
            System.out.printf("%s | %-11s | %-8.2f | %-12.2f | %s%n",
                    dt,
                    tx.getType(),
                    tx.getAmount(),
                    tx.getPostBalance(),
                    tx.getDescription());
        }
    }



}
