package com.arpith.mockpay.walletservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMessage {
    public final String EXCEPTION_OCCURRED = "Exception Occurred";
    public final String ACCESS_DENIED = "Access Denied";
    public final String JWT_TOKEN_VALID_ERROR = "Error Occurred during JWT Token Validation";
    public final String INVALID_AUTH_HEADER = "Invalid Header Value";
}
