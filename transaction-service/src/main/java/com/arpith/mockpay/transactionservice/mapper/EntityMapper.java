package com.arpith.mockpay.transactionservice.mapper;

import com.arpith.mockpay.transactionservice.dto.CreateTransactionRequest;
import com.arpith.mockpay.transactionservice.dto.CreateTransactionResponse;
import com.arpith.mockpay.transactionservice.dto.SecuredUser;
import com.arpith.mockpay.transactionservice.enumeration.Status;
import com.arpith.mockpay.transactionservice.model.Transaction;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class EntityMapper {
    private final Logger LOG = LoggerFactory.getLogger(EntityMapper.class);

    public Transaction toTransaction(CreateTransactionRequest transactionRequest, Long smallestDenominationFactor, SecuredUser securedUser) {
        LOG.info("Entering EntityMapper.toTransaction");
        return Transaction.builder()
                .senderId(securedUser.getUsername())
                .receiverId(transactionRequest.getReceiverId())
                .transactionId(UUID.randomUUID().toString())
                .status(Status.PENDING)
                .reason(transactionRequest.getReason())
                .amount((long) (transactionRequest.getAmount() * smallestDenominationFactor))
                .build();
    }

    public CreateTransactionResponse toCreateTransactionResponse(Transaction transaction, Long smallestDenominationFactor) {
        LOG.info("Entering EntityMapper.toCreateTransactionResponse");
        return CreateTransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .senderId(transaction.getSenderId())
                .receiverId(transaction.getReceiverId())
                .amount(transaction.getAmount() / smallestDenominationFactor)
                .status(transaction.getStatus().name())
                .reason(transaction.getReason())
                .build();
    }

    public List<CreateTransactionResponse> toCreateTransactionResponse(List<Transaction> transactions, Long smallestDenominationFactor) {
        var transactionsResponse = new ArrayList<CreateTransactionResponse>();
        for (var transaction : transactions) {
            transactionsResponse.add(CreateTransactionResponse.builder()
                    .transactionId(transaction.getTransactionId())
                    .senderId(transaction.getSenderId())
                    .receiverId(transaction.getReceiverId())
                    .amount(transaction.getAmount() / smallestDenominationFactor)
                    .status(transaction.getStatus().name())
                    .reason(transaction.getReason())
                    .build());
        }
        return transactionsResponse;
    }
}
