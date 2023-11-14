package com.arpith.mockpay.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletServiceApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "walletservice");
        SpringApplication.run(WalletServiceApplication.class, args);
    }
}