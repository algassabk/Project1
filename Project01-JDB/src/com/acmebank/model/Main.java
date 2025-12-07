package com.acmebank.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // 1) Load users from files (if any)
        List<User> users = UserFile.loadAllUsers();   // only ONCE
        AccountFile.loadAccounts(users);

        // 2) If no users yet, create default banker + customer and save them
        if (users.isEmpty()) {
            System.out.println("No users found. Creating default banker and customer...");

            String customerPwdHash = PasswordHelper.hash("1234");
            String bankerPwdHash   = PasswordHelper.hash("1111");

            Customer customer = new Customer("C001", "Khadija", customerPwdHash);
            Banker banker     = new Banker("B001", "Ahmed", bankerPwdHash, "Main Branch");

            // add a demo account so customer menu works
            CardType defaultCard = CardType.Mastercard;
            Account demoAccount = new CheckingAccount("ACC100001", 500.0, defaultCard);
            customer.addAccount(demoAccount);

            users.add(customer);
            users.add(banker);

            UserFile.saveCustomer(customer);
            UserFile.saveBanker(banker);
            AccountFile.saveAccountsForCustomer(customer);
        }

        Login loginService = new Login(users);

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== ACME Bank Login ===");
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine();

        Optional<User> loggedIn = loginService.login(id, pwd);

        if (loggedIn.isEmpty()) {
            System.out.println("Login failed. Program will exit.");
            return;
        }

        User user = loggedIn.get();
        System.out.println("Welcome " + user.getName() + " (" + user.getRole() + ")");

        Menu menu = new Menu(users);
        menu.showMainMenu(user);
    }

}
