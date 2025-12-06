package com.acmebank.model;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);

    // ======================
    // MAIN MENU (after login)
    // ======================
    public void showMainMenu(User user) {
        System.out.println("\n=== Welcome, " + user.getName() + " ===");

        if (user instanceof Banker) {
            showBankerMenu((Banker) user);
        } else if (user instanceof Customer) {
            showCustomerMenu((Customer) user);
        }
    }

    // ============
    // BANKER MENU
    // ============
    private void showBankerMenu(Banker banker) {
        while (true) {
            System.out.println("\n--- BANKER MENU ---");
            System.out.println("1. Add New Customer");
            System.out.println("2. View Customer Accounts");
            System.out.println("3. Logout");

            System.out.print("Enter choice: ");
            int ch = scanner.nextInt();
            scanner.nextLine(); // clean buffer

            switch (ch) {
                case 1 -> addNewCustomer();
                case 2 -> viewCustomerAccounts();
                case 3 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addNewCustomer() {
        System.out.println("[TODO] Add new customer logic will go here.");
    }

    private void viewCustomerAccounts() {
        System.out.println("[TODO] View accounts logic will go here.");
    }

    // ==============
    // CUSTOMER MENU
    // ==============
    private void showCustomerMenu(Customer customer) {
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View Transactions");
            System.out.println("6. Logout");

            System.out.print("Enter choice: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            switch (ch) {
                case 1 -> System.out.println("[TODO] Show balance.");
                case 2 -> System.out.println("[TODO] Deposit.");
                case 3 -> System.out.println("[TODO] Withdraw.");
                case 4 -> System.out.println("[TODO] Transfer.");
                case 5 -> System.out.println("[TODO] Transaction history.");
                case 6 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
