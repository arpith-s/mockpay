package org.arpith.mockpay.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "notificationservice");
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}