package com.example.transactionmanager.service;

import com.example.transactionmanager.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction("1", "Shopping", 150.0);
        transactionService.createTransaction(transaction);

        assertEquals(1, transactionService.listTransactions(0, 10).size());
    }

    @Test
    public void testDeleteTransaction() {
        Transaction transaction = new Transaction("1", "Shopping", 150.0);
        transactionService.createTransaction(transaction);

        assertEquals(1, transactionService.listTransactions(0, 10).size());

        transactionService.deleteTransaction("1");

        assertEquals(0, transactionService.listTransactions(0, 10).size());
    }

    @Test
    public void testModifyTransaction() {
        Transaction transaction = new Transaction("1", "Shopping", 150.0);
        transactionService.createTransaction(transaction);

        Transaction modifiedTransaction = new Transaction("1", "Shopping - Updated", 200.0);
        transactionService.modifyTransaction("1", modifiedTransaction);

        Transaction updatedTransaction = transactionService.listTransactions(0, 10).get(0);
        assertEquals("Shopping - Updated", updatedTransaction.getDescription());
        assertEquals(200.0, updatedTransaction.getAmount());
    }

    @Test
    public void testListTransactions() {
        Transaction transaction1 = new Transaction("1", "Shopping", 150.0);
        Transaction transaction2 = new Transaction("2", "Dining", 100.0);
        transactionService.createTransaction(transaction1);
        transactionService.createTransaction(transaction2);

        List<Transaction> transactions = transactionService.listTransactions(0, 10);
        assertEquals(2, transactions.size());
    }

    @Test
    public void testListTransactionsWithPagination() {
        // Create 15 transactions
        for (int i = 1; i <= 15; i++) {
            Transaction transaction = new Transaction(String.valueOf(i), "Transaction " + i, i * 10.0);
            transactionService.createTransaction(transaction);
        }

        // Test first page (10 items)
        List<Transaction> page1 = transactionService.listTransactions(0, 10);
        assertEquals(10, page1.size());
        assertEquals("1", page1.get(0).getId());
        assertEquals("13", page1.get(4).getId());
        assertEquals("4", page1.get(9).getId());

        // Test second page (5 items)
        List<Transaction> page2 = transactionService.listTransactions(1, 10);
        assertEquals(5, page2.size());
        assertEquals("5", page2.get(0).getId());
        assertEquals("9", page2.get(4).getId());

        // Test empty page
        List<Transaction> page3 = transactionService.listTransactions(2, 10);
        assertTrue(page3.isEmpty());
    }

    @Test
    public void testGetTotalTransactions() {
        assertEquals(0, transactionService.getTotalTransactions());

        // Create 5 transactions
        for (int i = 1; i <= 5; i++) {
            Transaction transaction = new Transaction(String.valueOf(i), "Transaction " + i, i * 10.0);
            transactionService.createTransaction(transaction);
        }

        assertEquals(5, transactionService.getTotalTransactions());
    }
}
