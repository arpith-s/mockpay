package com.arpith.mockpay.identityservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMessage {
    public final String APP_USER_CREATED = "User Created Successfully";

    public final String APP_USER_FETCHED = "User Fetched Successfully";

    public final String APP_USER_UPDATED = "User Update Successfully";

    public final String APP_USER_DELETED = "User Deletion Successfully";

    public final String FIELD_VALID_FAIL = "Field Validation Failed";

    public final String TOKEN_GENERATED = "Token Generated Successfully";

    public final String TOKEN_VERIFIED = "Token Verified Successfully";
}
