package com.arpith.mockpay.identityservice.service;

import com.arpith.mockpay.identityservice.enumeration.UserType;
import com.arpith.mockpay.identityservice.exception.InvalidAuthorityException;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SecuredUserService extends UserDetailsService {
    void create(SecuredUser securedUser, UserType userType);

    public SecuredUser update(SecuredUser securedUser, boolean isHashPassword) throws InvalidAuthorityException;

    void delete(String username);

    public SecuredUser findUserByUsername(String username) throws UsernameNotFoundException;
}
