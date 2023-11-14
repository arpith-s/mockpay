package org.arpith.mockpay.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
    @Value("${application.notification.mail.host}")
    String mailHost;
    @Value("${application.notification.mail.port}")
    Integer smtpPort;
    @Value("${application.notification.mail.username}")
    String mailUsername;
    @Value("${application.notification.mail.password}")
    String mailPassword;
    @Value("${application.notification.mail.protocol}")
    String mailProtocol;
    @Value("${application.notification.mail.smtp-auth}")
    String smtpAuth;
    @Value("${application.notification.mail.starttls-enable}")
    String starttlsEnable;
    @Value("${application.notification.mail.debugging}")
    String mailDebugging;

    @Bean
    SimpleMailMessage getMailMessage() {
        return new SimpleMailMessage();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(smtpPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        var properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", mailProtocol);
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.debug", mailDebugging);

        return mailSender;
    }

}
