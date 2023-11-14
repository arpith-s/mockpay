package com.arpith.mockpay.identityservice.service;

import com.arpith.mockpay.identityservice.exception.InvalidAuthorityException;
import com.arpith.mockpay.identityservice.exception.InvalidUserTypeException;
import com.arpith.mockpay.identityservice.exception.NonExistentAppUserException;
import com.arpith.mockpay.identityservice.exception.UniqueAppUserConstraintViolationException;
import com.arpith.mockpay.identityservice.model.AppUser;
import com.arpith.mockpay.identityservice.model.SecuredUser;

public interface AppUserService {
    void create(AppUser appUser, String userType) throws UniqueAppUserConstraintViolationException, InvalidUserTypeException;

    AppUser get(SecuredUser securedUser) throws NonExistentAppUserException;

    public void update(AppUser appUser, SecuredUser securedUser) throws UniqueAppUserConstraintViolationException, InvalidAuthorityException, NonExistentAppUserException, IllegalAccessException;

    public void deleteAppUserByEmail(String email) throws NonExistentAppUserException;

    void deleteAppUser(String message) throws NonExistentAppUserException;
}