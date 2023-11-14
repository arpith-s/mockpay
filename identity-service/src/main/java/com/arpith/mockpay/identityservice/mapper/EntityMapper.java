package com.arpith.mockpay.identityservice.mapper;

import com.arpith.mockpay.identityservice.dto.AppUserResponse;
import com.arpith.mockpay.identityservice.dto.CreateAppUserRequest;
import com.arpith.mockpay.identityservice.dto.SecuredUserResponse;
import com.arpith.mockpay.identityservice.dto.UpdateAppUserRequest;
import com.arpith.mockpay.identityservice.model.AppUser;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class EntityMapper {
    private static final Logger LOG = LoggerFactory.getLogger(EntityMapper.class);

    public AppUser toAppUser(CreateAppUserRequest createAppUserRequest) {
        LOG.info("Entering EntityMapper.toAppUser(CreateAppUserRequest)");
        return AppUser.builder()
                .name(createAppUserRequest.getName())
                .email(createAppUserRequest.getEmail())
                .age(createAppUserRequest.getAge())
                .mobile(createAppUserRequest.getMobile())
                .securedUser(
                        SecuredUser.builder()
                                .username(createAppUserRequest.getEmail())
                                .password(createAppUserRequest.getPassword())
                                .build()
                )
                .build();
    }

    public AppUser toAppUser(UpdateAppUserRequest updateAppUserRequest) {
        LOG.info("Entering EntityMapper.toAppUser(UpdateAppUserRequest)");
        return AppUser.builder()
                .name(updateAppUserRequest.getName())
                .email(updateAppUserRequest.getEmail())
                .age(updateAppUserRequest.getAge())
                .mobile(updateAppUserRequest.getMobile())
                .securedUser(
                        SecuredUser.builder()
                                .username(updateAppUserRequest.getEmail())
                                .password(updateAppUserRequest.getPassword())
                                .authorities(updateAppUserRequest.getAuthorities())
                                .isCredentialsNonExpired(updateAppUserRequest.getIsCredentialsNonExpired())
                                .isAccountNonExpired(updateAppUserRequest.getIsAccountNonExpired())
                                .isAccountNonLocked(updateAppUserRequest.getIsAccountNonLocked())
                                .isEnabled(updateAppUserRequest.getIsEnabled())
                                .build()
                )
                .build();
    }

    public AppUserResponse toAppUserResponse(AppUser appUser) {
        LOG.info("Entering EntityMapper.toAppUserResponse");
        return AppUserResponse.builder()
                .name(appUser.getName())
                .email(appUser.getEmail())
                .age(appUser.getAge())
                .mobile(appUser.getMobile())
                .build();
    }

    public SecuredUserResponse toSecuredUserResponse(SecuredUser securedUser) {
        LOG.info("Entering EntityMapper.toSecuredUserResponse");
        return SecuredUserResponse.builder()
                .username(securedUser.getUsername())
                .authorities(securedUser.getAuthoritiesAsString())
                .isAccountNonExpired(securedUser.getIsAccountNonExpired())
                .isAccountNonLocked(securedUser.getIsAccountNonLocked())
                .isCredentialsNonExpired(securedUser.getIsCredentialsNonExpired())
                .isEnabled(securedUser.getIsEnabled())
                .build();
    }

    public AppUser mapAppUserNonNullFields(AppUser sourceAppUser, AppUser destinationAppUser) {
        LOG.info("Entering EntityMapper.mapAppUserNonNullFields");
        if (sourceAppUser.getName() != null)
            destinationAppUser.setName(sourceAppUser.getName());
        if (sourceAppUser.getEmail() != null)
            destinationAppUser.setEmail(sourceAppUser.getEmail());
        if (sourceAppUser.getAge() != null)
            destinationAppUser.setAge(sourceAppUser.getAge());
        if (sourceAppUser.getMobile() != null)
            destinationAppUser.setMobile(sourceAppUser.getMobile());
        return destinationAppUser;
    }

    public SecuredUser mapSecuredUserNonNullFields(SecuredUser sourceSecuredUser, SecuredUser destinationSecuredUser) {
        LOG.info("Entering EntityMapper.mapSecuredUserNonNullFields");
        if (sourceSecuredUser.getUsername() != null)
            destinationSecuredUser.setUsername(sourceSecuredUser.getUsername());
        if (sourceSecuredUser.getPassword() != null)
            destinationSecuredUser.setPassword(sourceSecuredUser.getPassword());
        if (sourceSecuredUser.getAuthoritiesAsString() != null)
            destinationSecuredUser.setAuthorities(sourceSecuredUser.getAuthoritiesAsString());
        return destinationSecuredUser;
    }
}
