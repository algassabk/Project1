package com.acmebank.model;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void testSuccessfulLoginResetFailedAttempts() {
        String hash = PasswordHelper.hash("1234");
        Customer c = new Customer("C001", "Khadija", hash);
        Login login = new Login(List.of(c));

        Optional<User> result = login.login("C001", "1234");
        assertTrue(result.isPresent());
        assertEquals(0, c.getFailedAttempts());
    }

    @Test
    public void testThreeFailedAttemptsLocksUser() {
        String hash = PasswordHelper.hash("1234");
        Customer c = new Customer("C001", "Khadija", hash);
        Login login = new Login(List.of(c));

        login.login("C001", "wrong");
        login.login("C001", "wrong");
        login.login("C001", "wrong");

        assertEquals(3, c.getFailedAttempts());
        assertNotNull(c.getLockUntil());
    }

    @Test
    public void testCannotLoginWhileLocked() {
        String hash = PasswordHelper.hash("1234");
        Customer c = new Customer("C001", "Khadija", hash);
        Login login = new Login(List.of(c));

        c.setFailedAttempts(3);
        c.setLockUntil(LocalDateTime.now().plusMinutes(1));

        Optional<User> r = login.login("C001", "1234");
        assertTrue("Should not login while locked", r.isEmpty());
    }
}
