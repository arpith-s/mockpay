package com.arpith.mockpay.walletservice.controller;

import com.arpith.mockpay.walletservice.constant.ResponseMessage;
import com.arpith.mockpay.walletservice.dto.ResponseTemplate;
import com.arpith.mockpay.walletservice.dto.SecuredUser;
import com.arpith.mockpay.walletservice.mapper.EntityMapper;
import com.arpith.mockpay.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private static final Logger LOG = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @Value("${application.wallet.smallest-denomination-factor}")
    public Long smallestDenominationFactor;

    @GetMapping("/fetch")
    public ResponseEntity<ResponseTemplate<Object>> fetch(@AuthenticationPrincipal SecuredUser securedUser) {
        LOG.info("Entering WalletController.fetch");
        var walletResponse = EntityMapper.toWalletResponse(walletService.getWalletBalance(securedUser.getUsername()), smallestDenominationFactor);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseTemplate<>(HttpStatus.OK, ResponseMessage.WALLET_FETCH_SUCCESS, walletResponse));
    }
}
