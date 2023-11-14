package com.arpith.mockpay.identityservice.service.impl;

import com.arpith.mockpay.identityservice.constant.Constant;
import com.arpith.mockpay.identityservice.enumeration.UserType;
import com.arpith.mockpay.identityservice.exception.InvalidAuthorityException;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import com.arpith.mockpay.identityservice.repository.SecuredUserRepository;
import com.arpith.mockpay.identityservice.service.SecuredUserService;
import com.arpith.mockpay.identityservice.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecuredUserServiceImpl implements SecuredUserService {
    private static final Logger LOG = LoggerFactory.getLogger(SecuredUserServiceImpl.class);
    private final SecuredUserRepository securedUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(SecuredUser securedUser, UserType userType) {
        LOG.info("Entering SecuredUserService.create");
        var encryptedPassword = passwordEncoder.encode(securedUser.getPassword());
        var authorities = Utils.getAuthoritiesForUser(userType);

        securedUser.setPassword(encryptedPassword);
        securedUser.setAuthorities(authorities);
        securedUser.setIsAccountNonExpired(true);
        securedUser.setIsAccountNonLocked(true);
        securedUser.setIsCredentialsNonExpired(true);
        securedUser.setIsEnabled(true);
        securedUserRepository.save(securedUser);
    }

    @Override
    @Transactional
    public SecuredUser update(SecuredUser securedUser, boolean isHashPassword) throws InvalidAuthorityException {
        LOG.info("Entering SecuredUserService.update");
        if (!isValidAuthorities(securedUser.getAuthoritiesAsString())) throw new InvalidAuthorityException(securedUser.getAuthoritiesAsString());

        var encryptedPassword = isHashPassword ? passwordEncoder.encode(securedUser.getPassword()) : securedUser.getPassword();
        securedUser.setPassword(encryptedPassword);
        return securedUserRepository.save(securedUser);
    }

    @Override
    @Transactional
    public void delete(String username) {
        LOG.info("Entering SecuredUserService.delete");
        securedUserRepository.deleteSecuredUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Entering SecuredUserService.loadUserByUsername");
        return findUserByUsername(username);
    }

    @Override
    public SecuredUser findUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Entering SecuredUserService.findUserByUsername");
        var securedUser = securedUserRepository.findByUsername(username);
        return securedUser.orElseThrow(() -> new UsernameNotFoundException(
                String.format("%s with username '%s' does not exists!", SecuredUser.builder().build().getClass().getCanonicalName(), username)
        ));
    }

    private boolean isValidAuthorities(String authorities) {
        LOG.info("Entering SecuredUserService.isValidAuthorities: Authorities -> {}", authorities);
        var authoritiesList = List.of(authorities.split(Constant.DELIMITER_COMMA));
        for (var authority : authoritiesList) {
            authority = authority.trim();
            if (authority.isBlank() || !(Constant.SERVICE_AUTHORITIES.contains(authority) || Constant.CLIENT_AUTHORITIES.contains(authority)))
                return false;
        }
        return true;
    }

    public static boolean isBCryptEncoded(String input) {
        LOG.info("Entering SecuredUserService.isBCryptEncoded");
        return input.matches(Constant.BCRYPT_ENCODING_FORMAT);
    }
}