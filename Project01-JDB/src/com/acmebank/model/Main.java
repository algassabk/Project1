package com.acmebank.model;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Load users + accounts from files (once at startup)
        List<User> users = UserFile.loadAllUsers();
        AccountFile.loadAccounts(users);

        // If no users yet, create default banker + customer and save them
        if (users.isEmpty()) {
            System.out.println("No users found. Creating default banker and customer...");

            String customerPwdHash = PasswordHelper.hash("1234");
            String bankerPwdHash   = PasswordHelper.hash("1111");

            Customer customer = new Customer("C001", "Khadija", customerPwdHash);
            Banker banker     = new Banker("B001", "Ahmed", bankerPwdHash, "Main Branch");

            CardType defaultCard = CardType.Mastercard;
            Account demoAccount = new CheckingAccount("ACC100001", 500.0, defaultCard);
            customer.addAccount(demoAccount);

            users.add(customer);
            users.add(banker);

            UserFile.saveCustomer(customer);
            UserFile.saveBanker(banker);
            AccountFile.saveAccountsForCustomer(customer);
        }

        // Create login service + scanner
        Login loginService = new Login(users);
        Scanner scanner = new Scanner(System.in);

        // Login loop (fraud detection is inside Login.login)
        User loggedInUser = null;
        while (loggedInUser == null) {

            System.out.println("\n=== ACME Bank Login ===");
            System.out.print("Enter ID: ");
            String id = scanner.nextLine().trim();


            System.out.print("Enter Password: ");
            String pwd = scanner.nextLine();

            Optional<User> result = loginService.login(id, pwd);

            if (result.isPresent()) {
                loggedInUser = result.get();
            } else {
                // Login.login() already prints reason (wrong password, locked, etc.)
                System.out.println("Login failed. Try again.\n");
            }
        }

        //  Open menu for the logged-in user
        System.out.println("Welcome " + loggedInUser.getName() +
                " (" + loggedInUser.getRole() + ")");

        Menu menu = new Menu(users);
        menu.showMainMenu(loggedInUser);
    }
}
