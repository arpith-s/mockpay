package com.arpith.mockpay.identityservice.exception;

public class InvalidUserTypeException extends Exception {
    public InvalidUserTypeException(String userType) {
        super(String.format("User type '%s' is not valid", userType));
    }
}

