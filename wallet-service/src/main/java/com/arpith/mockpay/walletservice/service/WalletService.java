package com.arpith.mockpay.walletservice.service;

import com.arpith.mockpay.walletservice.exception.UniqueWalletConstraintViolationException;
import com.google.gson.JsonSyntaxException;

public interface WalletService {
    void createWallet(String message) throws JsonSyntaxException, UniqueWalletConstraintViolationException;

    void updateWallets(String message) throws JsonSyntaxException;

    void deleteWallet(String message) throws JsonSyntaxException;
}
