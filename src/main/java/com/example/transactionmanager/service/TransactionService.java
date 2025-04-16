package com.example.transactionmanager.service;

import com.example.transactionmanager.model.Transaction;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransactionService {
    // Use ConcurrentHashMap for thread safety
    private final ConcurrentMap<String, Transaction> transactions = new ConcurrentHashMap<>();

    // Add rate limiting control
    private final Semaphore semaphore = new Semaphore(1000); // Maximum concurrent requests
    private final AtomicInteger activeRequests = new AtomicInteger(0);
    private static final int MAX_ACTIVE_REQUESTS = 1000;
    private static final long TIMEOUT_MS = 1000; // 1 second timeout

    // Consider cache eviction/update strategies if caching list results
    @CacheEvict(value = "transactionsList", allEntries = true)
    public Transaction createTransaction(Transaction transaction) {
        if (!acquirePermit()) {
            throw new RuntimeException("Service is overloaded, please try again later");
        }
        try {
            // Perform additional validation
            validateTransaction(transaction);
            
            if (transaction.getId() == null || transaction.getId().isEmpty()) {
                transaction.setId(java.util.UUID.randomUUID().toString());
            }
            transactions.put(transaction.getId(), transaction);
            return transaction;
        } finally {
            releasePermit();
        }
    }

    @CacheEvict(value = "transactionsList", allEntries = true)
    public Optional<Transaction> deleteTransaction(String id) {
        if (!acquirePermit()) {
            throw new RuntimeException("Service is overloaded, please try again later");
        }
        try {
            // remove() is atomic
            Transaction removed = transactions.remove(id);
            return Optional.ofNullable(removed);
        } finally {
            releasePermit();
        }
    }

    // Use CachePut to update the cache for a specific item if needed, 
    // or just evict list cache
    @CacheEvict(value = "transactionsList", allEntries = true) 
    public Transaction modifyTransaction(String id, Transaction modifiedTransaction) {
        if (!acquirePermit()) {
            throw new RuntimeException("Service is overloaded, please try again later");
        }
        try {
            // Perform additional validation
            validateTransaction(modifiedTransaction);
            
            // computeIfPresent ensures atomicity for the get-and-update operation
            return transactions.computeIfPresent(id, (key, existingTransaction) -> {
                existingTransaction.setDescription(modifiedTransaction.getDescription());
                existingTransaction.setAmount(modifiedTransaction.getAmount());
                return existingTransaction;
            });
        } finally {
            releasePermit();
        }
    }

    // Cache the list result. Eviction happens on create/delete/modify
    @Cacheable("transactionsList")
    public List<Transaction> listTransactions(int page, int size) {
        if (!acquirePermit()) {
            throw new RuntimeException("Service is overloaded, please try again later");
        }
        try {
            List<Transaction> allTransactions = new ArrayList<>(transactions.values());
            allTransactions.sort(Comparator.comparing(Transaction::getId));
            int start = page * size;
            int end = Math.min(start + size, allTransactions.size());
            
            if (start >= allTransactions.size()) {
                return Collections.emptyList();
            }
            
            return allTransactions.subList(start, end);
        } finally {
            releasePermit();
        }
    }

    public long getTotalTransactions() {
        return transactions.size();
    }

    // Additional validation method for transaction data
    private void validateTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }
        
        if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction description cannot be empty");
        }
        
        if (transaction.getDescription().length() > 255) {
            throw new IllegalArgumentException("Transaction description cannot exceed 255 characters");
        }
    }

    private boolean acquirePermit() {
        try {
            if (activeRequests.incrementAndGet() > MAX_ACTIVE_REQUESTS) {
                activeRequests.decrementAndGet();
                return false;
            }
            return semaphore.tryAcquire(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            activeRequests.decrementAndGet();
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void releasePermit() {
        activeRequests.decrementAndGet();
        semaphore.release();
    }
}
