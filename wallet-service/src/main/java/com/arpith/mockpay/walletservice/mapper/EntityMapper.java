package com.arpith.mockpay.walletservice.mapper;

import com.arpith.mockpay.walletservice.dto.WalletResponse;
import com.arpith.mockpay.walletservice.model.Wallet;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class EntityMapper {
    private final Logger LOG = LoggerFactory.getLogger(EntityMapper.class);

    public WalletResponse toWalletResponse(Wallet wallet, Long smallestDenominationFactor) {
        return WalletResponse.builder()
                .walletId(wallet.getWalletId())
                .balance(wallet.getBalance() / smallestDenominationFactor)
                .currency(wallet.getCurrency())
                .build();
    }
}
