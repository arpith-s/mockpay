package com.arpith.mockpay.transactionservice.service;

import com.arpith.mockpay.transactionservice.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction transact(Transaction transaction);

    void updateTransaction(String message);

    List<Transaction> getAllTransactions(String senderId);
}
