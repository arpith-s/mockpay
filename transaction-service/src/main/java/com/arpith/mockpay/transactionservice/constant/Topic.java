package com.arpith.mockpay.transactionservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Topic {
    public final String TRANSACTION_CREATED = "TRANSACTION_CREATED";
    public final String WALLET_UPDATED = "WALLET_UPDATED";
    public final String TRANSACTION_COMPLETED = "TRANSACTION_COMPLETED";
}