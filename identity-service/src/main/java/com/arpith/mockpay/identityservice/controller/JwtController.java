package com.arpith.mockpay.identityservice.controller;

import com.arpith.mockpay.identityservice.constant.ResponseMessage;
import com.arpith.mockpay.identityservice.dto.AccessToken;
import com.arpith.mockpay.identityservice.dto.ResponseTemplate;
import com.arpith.mockpay.identityservice.exception.JwtTokenValidationException;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import com.arpith.mockpay.identityservice.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("v1/token")
@RequiredArgsConstructor
public class JwtController {
    private static final Logger LOG = LoggerFactory.getLogger(JwtController.class);
    private final JwtService jwtService;

    @GetMapping("/generate")
    public ResponseEntity<ResponseTemplate<AccessToken>> generateToken(@AuthenticationPrincipal SecuredUser securedUser) {
        LOG.info("Entering JwtController.generateToken");
        var tokenResponse = jwtService.createJwtTokenForUser(securedUser);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, ResponseMessage.TOKEN_GENERATED, tokenResponse));
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseTemplate<Map<String, String>>> verifyToken(@RequestBody @Valid AccessToken tokenRequest) throws JwtTokenValidationException, JsonProcessingException {
        LOG.info("Entering JwtController.generateToken");
        var encodedSecuredUser = jwtService.verifyJwtToken(tokenRequest);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, ResponseMessage.TOKEN_VERIFIED, Map.of("user_details", encodedSecuredUser)));
    }
}
