package com.arpith.mockpay.walletservice.exception;

public class UniqueWalletConstraintViolationException extends Exception {
    public UniqueWalletConstraintViolationException(String email) {
        super(String.format("Wallet with email '%s' already exists!", email));
    }
}

