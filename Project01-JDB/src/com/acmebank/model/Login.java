package com.acmebank.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Login {

    private final List<User> users;

    public Login(List<User> users) {
        this.users = users;
    }

    public Optional<User> login(String id, String password) {

        //Find user by ID
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (userOpt.isEmpty()) {
            System.out.println("User not found.");
            return Optional.empty();
        }

        User user = userOpt.get();

        //Check if account is locked
        if (user.getLockUntil() != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(user.getLockUntil())) {
                System.out.println("Account is locked. Please try again after: " + user.getLockUntil());
                return Optional.empty();
            } else {
                // lock period passed → reset lock info
                user.setLockUntil(null);
                user.setFailedAttempts(0);
            }
        }

        //Check password
        boolean passwordMatches =
                PasswordHelper.hash(password).equals(user.getEncryptedPassword());

        if (!passwordMatches) {
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            System.out.println("Wrong password. Failed attempts: " + attempts + "/3");

            if (attempts >= 3) {
                LocalDateTime lockTime = LocalDateTime.now().plusMinutes(1);
                user.setLockUntil(lockTime);
                System.out.println("Too many failed attempts. Account locked until: " + lockTime);
            }
            return Optional.empty();
        }

        //Success so reset counters
        user.setFailedAttempts(0);
        user.setLockUntil(null);

        return Optional.of(user);
    }
}
