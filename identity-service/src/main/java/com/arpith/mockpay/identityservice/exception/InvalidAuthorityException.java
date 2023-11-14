package com.arpith.mockpay.identityservice.exception;

public class InvalidAuthorityException extends Exception {
    public InvalidAuthorityException(String authorities) {
        super(String.format("One or more invalid authorities given: [%s]", authorities));
    }
}