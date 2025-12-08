package com.acmebank.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class BankerTest {

    @Test
    public void testBankerStoresBasicInfo() {
        Banker b = new Banker("B001", "Ahmed", "hashedPassword",
                "Main Branch");

        assertEquals("B001", b.getId());
        assertEquals("Ahmed", b.getName());
        assertEquals("hashedPassword", b.getEncryptedPassword());
        assertEquals("Main Branch", b.getBranchName());
        assertEquals(Role.Banker, b.getRole());
    }

    @Test
    public void testBankerRoleAndBranch() {
        Banker b = new Banker("B002", "Sara", "hashedPassword",
                "Manama Branch");

        assertEquals(Role.Banker, b.getRole());
        assertEquals("Manama Branch", b.getBranchName());
    }
}
