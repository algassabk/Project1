package com.acmebank.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TransactionTest {
    @Test
    public void testTransactionCreationTransferOut(){
        Transaction tx = new Transaction("TX001", "ACC001","ACC002",
                LocalDateTime.now(), TransactionType.Transfer_out, 100, 500, "Test Transfer out");

        assertEquals("TX001", tx.getTransactionId());
        assertEquals("ACC001", tx.getFromAccount());
        assertEquals("ACC002", tx.getToAccount());
        assertEquals(100, tx.getAmount(), 0.0001);
        assertEquals(TransactionType.Transfer_out, tx.getType());
    }
    @Test
    public void testTransactionCreationTransferIn(){
        Transaction tx = new Transaction("TX001", "ACC001","ACC002",
                LocalDateTime.now(), TransactionType.Transfer_in, 100, 500, "Test Transfer in");

        assertEquals(TransactionType.Transfer_in, tx.getType());
    }

}
