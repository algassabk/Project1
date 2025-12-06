package com.acmebank.model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public class Login {
    private final List<User> users;

    public Login(List<User> users) {
        this.users = users;
    }

    // try to login; returns User if success, empty if fail
    public Optional<User> login(String id, String password) {
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        return userOpt.flatMap(user -> {
            // 1) check if locked
            if (user.isLocked()) {
                System.out.println("Account is locked until: " + user.getLockedUntil());
                return Optional.empty();
            }

            // 2) check password
            if (PasswordHelper.check(password, user.getEncryptedPassword())) {
                user.resetFailedAttempts();
                System.out.println("Login successful.");
                return Optional.of(user);
            } else {
                System.out.println("Wrong password.");
                user.registerFailedAttempt();

                if (user.isLocked()) {
                    System.out.println("Too many attempts. Account locked for 1 minute.");
                }
                return Optional.empty();
            }
        });
    }
}
