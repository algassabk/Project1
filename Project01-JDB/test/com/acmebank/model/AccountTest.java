package com.acmebank.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    private Account createChecking(double balance) {
        return new CheckingAccount("ACC001", balance, CardType.Mastercard);
    }

    //Simple deposit
    @Test
    public void testDepositIncreasesBalance() throws Exception {
        Account acc = createChecking(100.0);

        acc.deposit(50.0);

        assertEquals(150.0, acc.getBalance(), 0.0001);
    }

    // Simple withdraw (no overdraft)
    @Test
    public void testWithdrawUpdatesBalance() throws Exception {
        Account acc = createChecking(200.0);

        acc.withdraw(50.0);

        assertEquals(150.0, acc.getBalance(), 0.0001);
        assertEquals(0, acc.getOverdraftCount());
        assertTrue(acc.isActive());
    }

    //First overdraft: account still active, overdraftCount = 1
    @Test
    public void testFirstOverdraftKeepsAccountActive() throws Exception {
        Account acc = createChecking(50.0);

        // 50 - 80 = -30, then -35 fee => -65
        acc.withdraw(80.0);

        assertEquals(-65.0, acc.getBalance(), 0.0001);
        assertEquals(1, acc.getOverdraftCount());
        assertTrue(acc.isActive());
    }

    // --- 4) After two overdrafts, account becomes inactive ---
    @Test
    public void testAccountDeactivatesAfterTwoOverdrafts() throws Exception {
        Account acc = createChecking(75.0);

        // First overdraft:
        // 75 - 80 = -5, then -35 fee => -40
        acc.withdraw(80.0);

        // Second overdraft:
        // -40 - 20 = -60, then -35 fee => -95 (still above -100 so allowed)
        acc.withdraw(20.0);

        assertEquals(-95.0, acc.getBalance(), 0.0001);
        assertEquals(2, acc.getOverdraftCount());
        assertFalse(acc.isActive());
    }

    // --- 5) Deposit after overdrafts reactivates and resets overdraftCount ---
    @Test
    public void testAccountReactivatesAfterDeposit() throws Exception {
        Account acc = createChecking(75.0);

        // Make two overdrafts to deactivate
        acc.withdraw(80.0);   // balance -40
        acc.withdraw(20.0);   // balance -95, overdraftCount=2, active=false

        assertFalse(acc.isActive());

        // Deposit enough to go positive: -95 + 200 = 105
        acc.deposit(200.0);

        assertTrue(acc.isActive());
        assertEquals(0, acc.getOverdraftCount());
        assertEquals(105.0, acc.getBalance(), 0.0001);
    }
}
