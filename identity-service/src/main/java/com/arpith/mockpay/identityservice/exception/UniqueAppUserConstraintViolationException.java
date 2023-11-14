package com.arpith.mockpay.identityservice.exception;

public class UniqueAppUserConstraintViolationException extends Exception {
    public UniqueAppUserConstraintViolationException(String email) {
        super(String.format("User with email '%s' already exists!", email));
    }
}

