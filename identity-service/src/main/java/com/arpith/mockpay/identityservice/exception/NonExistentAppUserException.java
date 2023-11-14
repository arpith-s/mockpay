package com.arpith.mockpay.identityservice.exception;

public class NonExistentAppUserException extends Exception {
    public NonExistentAppUserException(String username) {
        super(String.format("User with username: %s not found!", username));
    }
}
