package com.acmebank.model;

import java.time.LocalDateTime;

public abstract class User {

    protected String id;
    protected String name;
    protected Role role;
    protected String encryptedPassword;

    // for fraud detection
    protected int failedLoginAttempts;
    protected LocalDateTime lockedUntil;

    protected User(String id, String name, Role role, String encryptedPassword) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.encryptedPassword = encryptedPassword;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    // ====== these 3 methods are the important ones ======

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public void registerFailedAttempt() {
        failedLoginAttempts++;
        if (failedLoginAttempts >= 3) {
            lockedUntil = LocalDateTime.now().plusMinutes(1);
        }
    }

    public void resetFailedAttempts() {
        failedLoginAttempts = 0;
        lockedUntil = null;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }
}
