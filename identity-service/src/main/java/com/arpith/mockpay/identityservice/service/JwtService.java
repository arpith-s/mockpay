package com.arpith.mockpay.identityservice.service;

import com.arpith.mockpay.identityservice.dto.AccessToken;
import com.arpith.mockpay.identityservice.exception.JwtTokenValidationException;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface JwtService {
    public AccessToken createJwtTokenForUser(SecuredUser securedUser);

    public String verifyJwtToken(AccessToken accessToken) throws JwtTokenValidationException, JsonProcessingException;
}
