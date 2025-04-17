// src/main/java/com/example/transactionmanager/controller/TransactionController.java
package com.example.transactionmanager.controller;

import com.example.transactionmanager.model.Transaction;
import com.example.transactionmanager.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transaction Management", description = "Transaction Management System API") 
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create New Transaction", description = "Create a new transaction record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created transaction", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public Transaction createTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Transaction", description = "Delete a transaction record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted transaction", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<Transaction> deleteTransaction(
            @Parameter(description = "Transaction ID", required = true) 
            @PathVariable String id) {
        Optional<Transaction> transaction = transactionService.deleteTransaction(id);
        return transaction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modify Transaction", description = "Modify a transaction record by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully modified transaction", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<Transaction> modifyTransaction(
            @Parameter(description = "Transaction ID", required = true) 
            @PathVariable String id, 
            @Valid @RequestBody Transaction transaction) {
        Transaction modifiedTransaction = transactionService.modifyTransaction(id, transaction);
        return modifiedTransaction != null ? ResponseEntity.ok(modifiedTransaction) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Query Transaction List", description = "Query transaction records in pages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction list", 
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> listTransactions(
            @Parameter(description = "Page number (starting from 0)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.listTransactions(page, size);
        long total = transactionService.getTotalTransactions();
        
        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions);
        response.put("currentPage", page);
        response.put("totalItems", total);
        response.put("totalPages", (total + size - 1) / size);
        
        return ResponseEntity.ok(response);
    }
}
