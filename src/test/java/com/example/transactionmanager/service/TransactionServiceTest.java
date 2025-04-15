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
        Transaction transaction = new Transaction("1", "购物", 150.0);
        transactionService.createTransaction(transaction);

        assertEquals(1, transactionService.listTransactions().size());
    }

    @Test
    public void testDeleteTransaction() {
        Transaction transaction = new Transaction("1", "购物", 150.0);
        transactionService.createTransaction(transaction);

        assertEquals(1, transactionService.listTransactions().size());

        transactionService.deleteTransaction("1");

        assertEquals(0, transactionService.listTransactions().size());
    }

    @Test
    public void testModifyTransaction() {
        Transaction transaction = new Transaction("1", "购物", 150.0);
        transactionService.createTransaction(transaction);

        Transaction modifiedTransaction = new Transaction("1", "购物 - 更新", 200.0);
        transactionService.modifyTransaction("1", modifiedTransaction);

        Transaction updatedTransaction = transactionService.listTransactions().get(0);
        assertEquals("购物 - 更新", updatedTransaction.getDescription());
        assertEquals(200.0, updatedTransaction.getAmount());
    }

    @Test
    public void testListTransactions() {
        Transaction transaction1 = new Transaction("1", "购物", 150.0);
        Transaction transaction2 = new Transaction("2", "吃饭", 100.0);
        transactionService.createTransaction(transaction1);
        transactionService.createTransaction(transaction2);

        List<Transaction> transactions = transactionService.listTransactions();
        assertEquals(2, transactions.size());
    }
}
