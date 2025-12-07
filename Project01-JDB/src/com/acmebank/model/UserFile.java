package com.acmebank.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UserFile {

    private static final Path USERS_DIR = Paths.get("data", "users");

    private static void ensureDir() throws IOException {
        if (!Files.exists(USERS_DIR)) {
            Files.createDirectories(USERS_DIR);
        }
    }

    // ---------- File Names ----------
    private static Path fileForCustomer(Customer c) {
        String fileName = "Customer-" + c.getName() + "-" + c.getId() + ".txt";
        return USERS_DIR.resolve(fileName);
    }

    private static Path fileForBanker(Banker b) {
        String fileName = "Banker-" + b.getName() + "-" + b.getId() + ".txt";
        return USERS_DIR.resolve(fileName);
    }

    // ---------- SAVE ----------
    public static void saveCustomer(Customer c) {
        try {
            ensureDir();
            Path file = fileForCustomer(c);
            try (BufferedWriter w = Files.newBufferedWriter(file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                w.write("id=" + c.getId()); w.newLine();
                w.write("name=" + c.getName()); w.newLine();
                w.write("role=CUSTOMER"); w.newLine();
                w.write("passwordHash=" + c.getEncryptedPassword()); w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customer file: " + e.getMessage());
        }
    }

    public static void saveBanker(Banker b) {
        try {
            ensureDir();
            Path file = fileForBanker(b);
            try (BufferedWriter w = Files.newBufferedWriter(file,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                w.write("id=" + b.getId()); w.newLine();
                w.write("name=" + b.getName()); w.newLine();
                w.write("role=BANKER"); w.newLine();
                w.write("passwordHash=" + b.getEncryptedPassword()); w.newLine();
                w.write("branch=" + b.getBranchName()); w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving banker file: " + e.getMessage());
        }
    }

    // ---------- LOAD ----------
    public static List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();

        if (!Files.exists(USERS_DIR)) {
            return users; // empty first run
        }

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(USERS_DIR, "*.txt");
            for (Path file : stream) {
                List<String> lines = Files.readAllLines(file);
                String id = null, name = null, role = null, passwordHash = null, branch = null;

                for (String line : lines) {
                    String[] parts = line.split("=", 2);
                    if (parts.length != 2) continue;
                    switch (parts[0]) {
                        case "id" -> id = parts[1];
                        case "name" -> name = parts[1];
                        case "role" -> role = parts[1];
                        case "passwordHash" -> passwordHash = parts[1];
                        case "branch" -> branch = parts[1];
                    }
                }

                if (id == null || name == null || role == null || passwordHash == null) continue;

                if ("BANKER".equalsIgnoreCase(role)) {
                    users.add(new Banker(id, name, passwordHash, branch != null ? branch : "Main Branch"));
                } else {
                    users.add(new Customer(id, name, passwordHash));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }

        return users;
    }
}
