package com.arpith.mockpay.identityservice.service.impl;

import com.arpith.mockpay.identityservice.dto.AccessToken;
import com.arpith.mockpay.identityservice.exception.JwtTokenValidationException;
import com.arpith.mockpay.identityservice.mapper.EntityMapper;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import com.arpith.mockpay.identityservice.service.JwtService;
import com.arpith.mockpay.identityservice.service.SecuredUserService;
import com.arpith.mockpay.identityservice.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final Logger LOG = LoggerFactory.getLogger(JwtServiceImpl.class);
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final SecuredUserService securedUserService;

    @Override
    public AccessToken createJwtTokenForUser(SecuredUser securedUser) {
        LOG.info("Entering JwtService.createJwtTokenForUser");
        var token = jwtUtil.generateJwtToken(securedUser);
        return AccessToken.builder()
                .username(securedUser.getUsername())
                .token(token)
                .build();
    }

    @Override
    public String verifyJwtToken(AccessToken accessToken) throws JwtTokenValidationException, JsonProcessingException {
        LOG.info("Entering JwtService.verifyJwtToken");
        var token = accessToken.getToken().trim();
        var username = "";
        if (Boolean.TRUE.equals(jwtUtil.validateJwtToken(token))) {
            username = jwtUtil.extractJwtTokenUsername(token);
            var securedUser = securedUserService.findUserByUsername(username);
            if (securedUser != null) {
                var securedUserResponseJson = objectMapper.writeValueAsString(EntityMapper.toSecuredUserResponse(securedUser));
                return Base64.getEncoder().encodeToString(securedUserResponseJson.getBytes());
            }
        }
        throw new JwtTokenValidationException(token, accessToken.getUsername());
    }
}
