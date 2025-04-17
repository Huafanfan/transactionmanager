// src/main/java/com/example/transactionmanager/model/Transaction.java
package com.example.transactionmanager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Transaction data model")
public class Transaction {
    @Schema(description = "Transaction ID", example = "1234-5678-90ab-cdef", required = true)
    @NotBlank(message = "Transaction ID cannot be blank")
    private String id;
    
    @Schema(description = "Transaction description", example = "Purchase of Apple iPhone", required = true)
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;
    
    @Schema(description = "Transaction amount", example = "1299.99", minimum = "0")
    @NotNull(message = "Amount cannot be null")
    @PositiveOrZero(message = "Amount cannot be negative")
    private double amount;

    public Transaction() {}

    public Transaction(String id, String description, double amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
