package org.arpith.mockpay.notificationservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Topic {
    public final String TRANSACTION_COMPLETED = "TRANSACTION_COMPLETED";
    public final String WALLET_CREATED = "WALLET_CREATED";
    public final String WALLET_DELETED = "WALLET_DELETED";
}