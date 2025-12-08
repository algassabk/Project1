package com.acmebank.model;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class CustomerTest {
    @Test
    public void testNewCustomerStartsWithNoAccount(){
        Customer c = new Customer("C001","Huda", "hashedPassword");
        List <Account> accounts = c.getAccounts();

        assertNotNull(accounts);
        assertEquals(0, accounts.size());
    }

    @Test
    public void testAddAccountAddsToCustomerList(){
        Customer c = new Customer("C001", "Ali","hashedPassword");
        Account acc1 = new CheckingAccount("CHK001", 500.0 , CardType.Mastercard);
        Account acc2 = new SavingsAccount("SAV001", 1000.0 , CardType.Mastercard);

        c.addAccount(acc1);
        c.addAccount(acc2);

        List<Account> accounts = c.getAccounts();

        assertEquals(2, accounts.size());
        assertSame(acc1, accounts.get(0));
        assertSame(acc2, accounts.get(1));
    }
}
