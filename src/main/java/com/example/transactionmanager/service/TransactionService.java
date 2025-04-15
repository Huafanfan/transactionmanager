package com.example.transactionmanager.service;

import com.example.transactionmanager.model.Transaction;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();

    public Transaction createTransaction(Transaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    public Optional<Transaction> deleteTransaction(String id) {
        Transaction transaction = getTransactionById(id);
        if (transaction != null) {
            transactions.remove(transaction);
            return Optional.of(transaction);
        }
        return Optional.empty();
    }

    public Transaction modifyTransaction(String id, Transaction modifiedTransaction) {
        Transaction transaction = getTransactionById(id);
        if (transaction != null) {
            transaction.setDescription(modifiedTransaction.getDescription());
            transaction.setAmount(modifiedTransaction.getAmount());
            return transaction;
        }
        return null;
    }

    @Cacheable("transactions")
    public List<Transaction> listTransactions() {
        return transactions;
    }

    private Transaction getTransactionById(String id) {
        return transactions.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
