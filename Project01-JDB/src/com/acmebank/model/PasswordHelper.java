package com.acmebank.model;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordHelper {
    // change normal password to hash
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // check if entered password matches saved hash
    public static boolean check(String plainPassword, String savedHash) {
        return hash(plainPassword).equals(savedHash);
    }
}
