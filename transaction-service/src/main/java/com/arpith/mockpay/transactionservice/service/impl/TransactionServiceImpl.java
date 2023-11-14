package com.arpith.mockpay.transactionservice.service.impl;

import com.arpith.mockpay.transactionservice.constant.Constant;
import com.arpith.mockpay.transactionservice.constant.DataFieldKeys;
import com.arpith.mockpay.transactionservice.constant.Topic;
import com.arpith.mockpay.transactionservice.enumeration.Status;
import com.arpith.mockpay.transactionservice.model.Transaction;
import com.arpith.mockpay.transactionservice.repository.TransactionCacheRepository;
import com.arpith.mockpay.transactionservice.repository.TransactionRepository;
import com.arpith.mockpay.transactionservice.service.TransactionService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final TransactionCacheRepository transactionCacheRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Override
    public Transaction transact(Transaction transaction) {
        LOG.info("Entering TransactionService.transact");
        transactionRepository.save(transaction);

        var transactionMap = new HashMap<String, Object>();
        transactionMap.put(DataFieldKeys.SENDER_ID, transaction.getSenderId());
        transactionMap.put(DataFieldKeys.RECEIVER_ID, transaction.getReceiverId());
        transactionMap.put(DataFieldKeys.AMOUNT, transaction.getAmount());
        transactionMap.put(DataFieldKeys.TRANSACTION_ID, transaction.getTransactionId());

        transactionCacheRepository.invalidateCache(transaction.getSenderId());
        kafkaTemplate.send(Topic.TRANSACTION_CREATED, gson.toJson(transactionMap));
        return transaction;
    }

    @Override
    @KafkaListener(topics = {Topic.WALLET_UPDATED}, groupId = Constant.CONSUMER_GROUP_ID)
    public void updateTransaction(String message) {
        LOG.info("Entering TransactionService.updateTransaction");
        Map<String, Object> transactionMap = gson.fromJson(message, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        var walletUpdateStatus = (String) transactionMap.get(DataFieldKeys.STATUS);
        var transactionId = (String) transactionMap.get(DataFieldKeys.TRANSACTION_ID);

        Status status;
        if (walletUpdateStatus.equals(Status.SUCCESSFUL.name())) {
            status = Status.SUCCESSFUL;
            transactionRepository.updateTransaction(transactionId, status);
        } else {
            status = Status.FAILED;
            transactionRepository.updateTransaction(transactionId, status);
        }

        transactionCacheRepository.invalidateCache((String) transactionMap.get(DataFieldKeys.SENDER_ID));
        transactionMap.put(DataFieldKeys.STATUS, status.name());
        kafkaTemplate.send(Topic.TRANSACTION_COMPLETED, gson.toJson(transactionMap));
    }

    @Override
    public List<Transaction> getAllTransactions(String senderId) {
        LOG.info("Entering TransactionService.getAllTransactions");
        var cachedTransactions = transactionCacheRepository.get(senderId);
        if (cachedTransactions != null) return cachedTransactions;
        var transactions = transactionRepository.findBySenderId(senderId);
        transactionCacheRepository.set(senderId, transactions);
        return transactions;
    }
}
