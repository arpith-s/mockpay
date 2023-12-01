package com.arpith.mockpay.walletservice.service;

import com.arpith.mockpay.walletservice.dto.AccessToken;
import com.arpith.mockpay.walletservice.dto.ResponseTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

public interface IdentityServiceClient {
    @PostExchange("/token/verify")
    ResponseTemplate<Map<String, String>> authenticate(@RequestHeader Map<String, String> headers, @RequestBody AccessToken accessToken);
}
