package com.example.transactionmanager.controller;

import com.example.transactionmanager.model.Transaction;
import com.example.transactionmanager.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTransaction() throws Exception {
        Transaction transaction = new Transaction("1", "购物", 150.0);
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("购物"))
                .andExpect(jsonPath("$.amount").value(150.0));

        verify(transactionService).createTransaction(any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        Transaction transaction = new Transaction("1", "购物", 150.0);
        when(transactionService.deleteTransaction("1")).thenReturn(Optional.of(transaction));

        mockMvc.perform(delete("/transactions/1"))
                .andExpect(status().isOk());

        verify(transactionService).deleteTransaction("1");
    }

    @Test
    public void testDeleteTransactionNotFound() throws Exception {
        when(transactionService.deleteTransaction("999")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/transactions/999"))
                .andExpect(status().isNotFound());

        verify(transactionService).deleteTransaction("999");
    }

    @Test
    public void testModifyTransaction() throws Exception {
        Transaction modifiedTransaction = new Transaction("1", "购物 - 更新", 200.0);
        when(transactionService.modifyTransaction(eq("1"), any(Transaction.class))).thenReturn(modifiedTransaction);

        mockMvc.perform(put("/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedTransaction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("购物 - 更新"))
                .andExpect(jsonPath("$.amount").value(200.0));

        verify(transactionService).modifyTransaction(eq("1"), any(Transaction.class));
    }

    @Test
    public void testModifyTransactionNotFound() throws Exception {
        when(transactionService.modifyTransaction(eq("999"), any(Transaction.class))).thenReturn(null);

        mockMvc.perform(put("/transactions/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Transaction())))
                .andExpect(status().isNotFound());

        verify(transactionService).modifyTransaction(eq("999"), any(Transaction.class));
    }

    @Test
    public void testListTransactions() throws Exception {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("1", "购物", 150.0),
                new Transaction("2", "吃饭", 100.0)
        );
        when(transactionService.listTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));

        verify(transactionService).listTransactions();
    }
}