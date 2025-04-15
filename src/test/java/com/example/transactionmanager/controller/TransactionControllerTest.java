package com.example.transactionmanager.controller;

import com.example.transactionmanager.model.Transaction;
import com.example.transactionmanager.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        transactionService = Mockito.mock(TransactionService.class);
    }

    @Test
    public void testCreateTransaction() throws Exception {
        String json = "{\"id\":\"1\",\"description\":\"购物\",\"amount\":150.0}";

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(transactionService).createTransaction(any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/transactions/1"))
                .andExpect(status().isOk());

        verify(transactionService).deleteTransaction("1");
    }

    @Test
    public void testModifyTransaction() throws Exception {
        String json = "{\"description\":\"购物 - 更新\",\"amount\":200.0}";

        mockMvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(transactionService).modifyTransaction(any(String.class), any(Transaction.class));
    }

    @Test
    public void testListTransactions() throws Exception {
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk());
    }
}
