package com.arpith.mockpay.identityservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMessage {
    public final String EXCEPTION_OCCURRED = "Exception Occurred";
    public final String ACCESS_DENIED = "Access Denied";
    public final String ILLEGAL_ARGUMENT = "Illegal Argument Exception";
    public final String EXPIRED_JWT = "Given JWT Token is Expired";
    public final String MALFORMED_JWT = "Malformed JWT Token";
    public final String JWT_TOKEN_VALID_ERROR = "Error Occurred during JWT Token Validation";
    public final String INVALID_AUTH_HEADER = "Invalid Header Value";
}
