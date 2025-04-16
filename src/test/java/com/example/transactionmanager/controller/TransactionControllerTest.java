package com.example.transactionmanager.controller;

import com.example.transactionmanager.model.Transaction;
import com.example.transactionmanager.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction("1", "Shopping", 150.0);
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionController.createTransaction(transaction);
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Shopping", result.getDescription());
        assertEquals(150.0, result.getAmount());

        verify(transactionService).createTransaction(transaction);
    }

    @Test
    public void testDeleteTransaction() {
        Transaction transaction = new Transaction("1", "Shopping", 150.0);
        when(transactionService.deleteTransaction("1")).thenReturn(Optional.of(transaction));

        ResponseEntity<Transaction> response = transactionController.deleteTransaction("1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transaction, response.getBody());

        verify(transactionService).deleteTransaction("1");
    }

    @Test
    public void testDeleteNonExistentTransaction() {
        when(transactionService.deleteTransaction("1")).thenReturn(Optional.empty());

        ResponseEntity<Transaction> response = transactionController.deleteTransaction("1");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(transactionService).deleteTransaction("1");
    }

    @Test
    public void testModifyTransaction() {
        Transaction existingTransaction = new Transaction("1", "Shopping", 150.0);
        Transaction modifiedTransaction = new Transaction("1", "Shopping - Updated", 200.0);
        when(transactionService.modifyTransaction("1", modifiedTransaction)).thenReturn(modifiedTransaction);

        ResponseEntity<Transaction> response = transactionController.modifyTransaction("1", modifiedTransaction);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(modifiedTransaction, response.getBody());

        verify(transactionService).modifyTransaction("1", modifiedTransaction);
    }

    @Test
    public void testModifyNonExistentTransaction() {
        Transaction modifiedTransaction = new Transaction("1", "Shopping - Updated", 200.0);
        when(transactionService.modifyTransaction("1", modifiedTransaction)).thenReturn(null);

        ResponseEntity<Transaction> response = transactionController.modifyTransaction("1", modifiedTransaction);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(transactionService).modifyTransaction("1", modifiedTransaction);
    }

    @Test
    public void testListTransactionsWithPagination() {
        // Prepare mock data
        List<Transaction> mockTransactions = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            mockTransactions.add(new Transaction(String.valueOf(i), "Transaction " + i, i * 10.0));
        }

        // Mock service behavior
        when(transactionService.listTransactions(0, 10)).thenReturn(mockTransactions.subList(0, 10));
        when(transactionService.getTotalTransactions()).thenReturn(15L);

        // Test first page
        ResponseEntity<Map<String, Object>> response = transactionController.listTransactions(0, 10);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        
        @SuppressWarnings("unchecked")
        List<Transaction> transactions = (List<Transaction>) responseBody.get("transactions");
        assertEquals(10, transactions.size());
        assertEquals(0, responseBody.get("currentPage"));
        assertEquals(15L, responseBody.get("totalItems"));
        assertEquals(2L, responseBody.get("totalPages"));

        // Verify service calls
        verify(transactionService).listTransactions(0, 10);
        verify(transactionService).getTotalTransactions();
    }

    @Test
    public void testListTransactionsEmptyResult() {
        // Mock empty result
        when(transactionService.listTransactions(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(transactionService.getTotalTransactions()).thenReturn(0L);

        // Test empty result
        ResponseEntity<Map<String, Object>> response = transactionController.listTransactions(0, 10);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        
        @SuppressWarnings("unchecked")
        List<Transaction> transactions = (List<Transaction>) responseBody.get("transactions");
        assertTrue(transactions.isEmpty());
        assertEquals(0, responseBody.get("currentPage"));
        assertEquals(0L, responseBody.get("totalItems"));
        assertEquals(0L, responseBody.get("totalPages"));
    }

    @Test
    public void testListTransactionsLastPage() {
        // Prepare mock data for last page
        List<Transaction> mockTransactions = new ArrayList<>();
        for (int i = 11; i <= 15; i++) {
            mockTransactions.add(new Transaction(String.valueOf(i), "Transaction " + i, i * 10.0));
        }

        // Mock service behavior
        when(transactionService.listTransactions(1, 10)).thenReturn(mockTransactions);
        when(transactionService.getTotalTransactions()).thenReturn(15L);

        // Test last page
        ResponseEntity<Map<String, Object>> response = transactionController.listTransactions(1, 10);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        
        @SuppressWarnings("unchecked")
        List<Transaction> transactions = (List<Transaction>) responseBody.get("transactions");
        assertEquals(5, transactions.size());
        assertEquals(1, responseBody.get("currentPage"));
        assertEquals(15L, responseBody.get("totalItems"));
        assertEquals(2L, responseBody.get("totalPages"));
    }
}