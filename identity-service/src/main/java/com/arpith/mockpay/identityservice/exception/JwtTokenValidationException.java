package com.arpith.mockpay.identityservice.exception;

public class JwtTokenValidationException extends Exception {
    public JwtTokenValidationException(String token, String username) {
        super(String.format("Invalid token '%s' for User '%s'", token, username));
    }
}
