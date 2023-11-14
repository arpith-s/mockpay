package com.arpith.mockpay.identityservice;

import com.arpith.mockpay.identityservice.enumeration.UserType;
import com.arpith.mockpay.identityservice.model.AppUser;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import com.arpith.mockpay.identityservice.repository.AppUserRepository;
import com.arpith.mockpay.identityservice.service.SecuredUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class IdentityServiceApplication implements CommandLineRunner {
    private final AppUserRepository appUserRepository;
    private final SecuredUserService securedUserService;

    @Value("${application.security.service.user-name}")
    String serviceUserName;

    @Value("${application.security.service.email}")
    String serviceUserEmail;

    @Value("${application.security.service.secret}")
    String serviceUserSecret;

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "identityservice");
        SpringApplication.run(IdentityServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var appUser = AppUser.builder()
                .name(serviceUserName)
                .email(serviceUserEmail)
                .securedUser(
                        SecuredUser.builder()
                                .username(serviceUserEmail)
                                .password(serviceUserSecret)
                                .build())
                .build();
        var securedUser = appUser.getSecuredUser();
        securedUserService.create(securedUser, Enum.valueOf(UserType.class, UserType.SERVICE.name()));
        appUser.setSecuredUser(securedUser);
        appUserRepository.save(appUser);
    }
}