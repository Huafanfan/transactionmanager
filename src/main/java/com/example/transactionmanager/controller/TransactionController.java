// src/main/java/com/example/transactionmanager/controller/TransactionController.java
package com.example.transactionmanager.controller;

import com.example.transactionmanager.model.Transaction;
import com.example.transactionmanager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable String id) {
        Optional<Transaction> transaction = transactionService.deleteTransaction(id);
        return transaction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> modifyTransaction(@PathVariable String id, @RequestBody Transaction transaction) {
        Transaction modifiedTransaction = transactionService.modifyTransaction(id, transaction);
        return modifiedTransaction != null ? ResponseEntity.ok(modifiedTransaction) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Transaction> listTransactions() {
        return transactionService.listTransactions();
    }
}
