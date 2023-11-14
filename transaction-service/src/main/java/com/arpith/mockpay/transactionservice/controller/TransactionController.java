package com.arpith.mockpay.transactionservice.controller;

import com.arpith.mockpay.transactionservice.constant.ResponseMessage;
import com.arpith.mockpay.transactionservice.dto.CreateTransactionRequest;
import com.arpith.mockpay.transactionservice.dto.ResponseTemplate;
import com.arpith.mockpay.transactionservice.dto.SecuredUser;
import com.arpith.mockpay.transactionservice.mapper.EntityMapper;
import com.arpith.mockpay.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    @Value("${application.transaction.smallest-denomination-factor}")
    public Long smallestDenominationFactor;

    @PostMapping("/transact")
    public ResponseEntity<ResponseTemplate<Object>> transact(@RequestBody @Valid CreateTransactionRequest transactionRequest, @AuthenticationPrincipal SecuredUser securedUser) {
        LOG.info("Entering TransactionController.transact");
        var transactionResponse = transactionService.transact(EntityMapper.toTransaction(transactionRequest, smallestDenominationFactor, securedUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseTemplate<>(HttpStatus.CREATED, ResponseMessage.TRANSACTION_INIT,
                        EntityMapper.toCreateTransactionResponse(transactionResponse, smallestDenominationFactor)));
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseTemplate<Object>> fetch(@AuthenticationPrincipal SecuredUser securedUser) {
        LOG.info("Entering TransactionController.fetch");
        var transactionsResponse = EntityMapper.toCreateTransactionResponse(transactionService.getAllTransactions(securedUser.getUsername()), smallestDenominationFactor);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseTemplate<>(HttpStatus.OK, ResponseMessage.TRANSACTIONS_FETCHED, transactionsResponse));
    }
}
