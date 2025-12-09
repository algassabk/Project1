package com.acmebank.model;

import java.time.LocalDateTime;

public abstract class User {

    protected String id;
    protected String name;
    protected Role role;
    protected String encryptedPassword;

    //fraud detection
    protected int failedAttempts;
    protected LocalDateTime lockUntil;

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

    //fraud detection support
    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getLockUntil() {
        return lockUntil;
    }

    public void setLockUntil(LocalDateTime lockUntil) {
        this.lockUntil = lockUntil;
    }
}
