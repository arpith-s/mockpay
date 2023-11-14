package com.arpith.mockpay.transactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionServiceApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "transactionservice");
        SpringApplication.run(TransactionServiceApplication.class, args);
    }
}