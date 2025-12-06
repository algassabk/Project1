package com.acmebank.model;

import java.util.ArrayList;   //  added
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // 1) Demo users in memory for now
        String customerPwdHash = PasswordHelper.hash("1234");
        String bankerPwdHash   = PasswordHelper.hash("1111");

        Customer customer = new Customer("C001", "Khadija", customerPwdHash);
        Banker banker     = new Banker("B001", "Ahmed", bankerPwdHash, "Main Branch");

        // ✅ use modifiable list instead of List.of(...)
        List<User> users = new ArrayList<>();
        users.add(customer);
        users.add(banker);



        Login loginService = new Login(users);

        // 2) Ask for login
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== ACME Bank Login ===");
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine();

        Optional<User> loggedIn = loginService.login(id, pwd);

        // 3) Check result
        if (loggedIn.isEmpty()) {
            System.out.println("Login failed. Program will exit.");
            return;
        }

        User user = loggedIn.get();
        System.out.println("Welcome " + user.getName() + " (" + user.getRole() + ")");

        // 4) Open menu for this user
        // ✅ pass the users list into Menu, so it can add new customers
        Menu menu = new Menu(users);
        menu.showMainMenu(user);   // This method will decide banker vs customer
    }
}
