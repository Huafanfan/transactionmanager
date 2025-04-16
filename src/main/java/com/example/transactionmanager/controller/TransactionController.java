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
@Tag(name = "交易管理", description = "交易管理系统的API接口")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "创建新交易", description = "创建一个新的交易记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功创建交易", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "400", description = "无效的请求参数")
    })
    public Transaction createTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除交易", description = "根据ID删除交易记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功删除交易", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "404", description = "交易不存在")
    })
    public ResponseEntity<Transaction> deleteTransaction(
            @Parameter(description = "交易ID", required = true) 
            @PathVariable String id) {
        Optional<Transaction> transaction = transactionService.deleteTransaction(id);
        return transaction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改交易", description = "根据ID修改交易记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功修改交易", 
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
        @ApiResponse(responseCode = "404", description = "交易不存在"),
        @ApiResponse(responseCode = "400", description = "无效的请求参数")
    })
    public ResponseEntity<Transaction> modifyTransaction(
            @Parameter(description = "交易ID", required = true) 
            @PathVariable String id, 
            @Valid @RequestBody Transaction transaction) {
        Transaction modifiedTransaction = transactionService.modifyTransaction(id, transaction);
        return modifiedTransaction != null ? ResponseEntity.ok(modifiedTransaction) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "查询交易列表", description = "分页查询交易记录")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取交易列表", 
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Map<String, Object>> listTransactions(
            @Parameter(description = "页码(从0开始)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") 
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
