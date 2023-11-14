package com.arpith.mockpay.identityservice.service.impl;

import com.arpith.mockpay.identityservice.constant.Constant;
import com.arpith.mockpay.identityservice.constant.DataFieldKeys;
import com.arpith.mockpay.identityservice.constant.Topic;
import com.arpith.mockpay.identityservice.enumeration.Status;
import com.arpith.mockpay.identityservice.enumeration.UserType;
import com.arpith.mockpay.identityservice.exception.InvalidAuthorityException;
import com.arpith.mockpay.identityservice.exception.InvalidUserTypeException;
import com.arpith.mockpay.identityservice.exception.NonExistentAppUserException;
import com.arpith.mockpay.identityservice.exception.UniqueAppUserConstraintViolationException;
import com.arpith.mockpay.identityservice.mapper.EntityMapper;
import com.arpith.mockpay.identityservice.model.AppUser;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import com.arpith.mockpay.identityservice.repository.AppUserCacheRepository;
import com.arpith.mockpay.identityservice.repository.AppUserRepository;
import com.arpith.mockpay.identityservice.repository.SecuredUserRepository;
import com.arpith.mockpay.identityservice.service.AppUserService;
import com.arpith.mockpay.identityservice.service.SecuredUserService;
import com.arpith.mockpay.identityservice.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private static final Logger LOG = LoggerFactory.getLogger(AppUserServiceImpl.class);
    private final AppUserRepository appUserRepository;
    private final SecuredUserRepository securedUserRepository;
    private final SecuredUserService securedUserService;
    private final AppUserCacheRepository appUserCacheRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Override
    public void create(AppUser appUser, String userType) throws UniqueAppUserConstraintViolationException, InvalidUserTypeException {
        LOG.info("Entering AppUserService.create");
        if (appUserRepository.findByEmail(appUser.getEmail()).isPresent())
            throw new UniqueAppUserConstraintViolationException(appUser.getEmail());

        if (!Utils.isValidEnum(userType, UserType.class))
            throw new InvalidUserTypeException(userType);

        var securedUser = appUser.getSecuredUser();
        securedUserService.create(securedUser, Enum.valueOf(UserType.class, userType));

        appUser.setSecuredUser(securedUser);
        appUserRepository.save(appUser);

        var appUserMap = new HashMap<String, Object>();
        appUserMap.put(DataFieldKeys.NAME, appUser.getName());
        appUserMap.put(DataFieldKeys.EMAIL, appUser.getEmail());
        var appUserJson = gson.toJson(appUserMap);
        kafkaTemplate.send(Topic.APP_USER_CREATED, appUserJson);
    }

    @Override
    public AppUser get(SecuredUser securedUser) throws NonExistentAppUserException {
        LOG.info("Entering AppUserService.get");
        var username = securedUser.getUsername();
        var cachedAppUser = appUserCacheRepository.get(Math.toIntExact(securedUser.getId()));
        if (cachedAppUser != null) return cachedAppUser;

        var appUser = appUserRepository.findByEmail(username).orElseThrow(() -> new NonExistentAppUserException(username));
        appUserCacheRepository.set(appUser);
        return appUser;
    }

    @Override
    @Transactional
    public void update(AppUser appUser, SecuredUser securedUser) throws UniqueAppUserConstraintViolationException, InvalidAuthorityException, NonExistentAppUserException, IllegalAccessException {
        var username = securedUser.getUsername();
        LOG.info("Entering AppUserService.update");
        if (appUserRepository.findByEmail(appUser.getEmail()).isPresent())
            throw new UniqueAppUserConstraintViolationException(appUser.getEmail());

        var existingAppUser = appUserRepository.findByEmail(username).orElseThrow(() -> new NonExistentAppUserException(username));
        var existingSecuredUser = existingAppUser.getSecuredUser();
        var updatedPassword = appUser.getSecuredUser().getPassword();

        if (!Utils.areAllFieldsNull(appUser)) {
            existingSecuredUser = EntityMapper.mapSecuredUserNonNullFields(appUser.getSecuredUser(), existingSecuredUser);
            existingSecuredUser = securedUserService.update(existingSecuredUser, (updatedPassword != null && !updatedPassword.isBlank()));

            existingAppUser.setSecuredUser(existingSecuredUser);
            existingAppUser = EntityMapper.mapAppUserNonNullFields(appUser, existingAppUser);
            appUserRepository.save(existingAppUser);
        }
    }

    @Override
    @Transactional
    public void deleteAppUserByEmail(String email) throws NonExistentAppUserException {
        LOG.info("Entering AppUserService.deleteAppUserByEmail");
        var appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isPresent()) {
            var appUser = appUserOptional.get();
            var securedUserId = appUser.getSecuredUser().getId();
            appUserRepository.delete(appUser);
            securedUserRepository.deleteById(securedUserId);

            var appUserMap = new HashMap<String, Object>();
            appUserMap.put(DataFieldKeys.NAME, appUser.getName());
            appUserMap.put(DataFieldKeys.EMAIL, appUser.getEmail());
            kafkaTemplate.send(Topic.APP_USER_DELETED, gson.toJson(appUserMap));
        } else {
            throw new NonExistentAppUserException(email);
        }
    }

    @Override
    @Transactional
    @KafkaListener(topics = {Topic.WALLET_CREATED}, groupId = Constant.CONSUMER_GROUP_ID)
    public void deleteAppUser(String message) throws NonExistentAppUserException {
        LOG.info("Entering AppUserService.deleteAppUser");
        Map<String, Object> appUserMap = gson.fromJson(message, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        var email = (String) appUserMap.get(DataFieldKeys.EMAIL);
        var status = (String) appUserMap.get(DataFieldKeys.STATUS);
        if (status.equals(Status.FAILED.name())) {
            appUserRepository.findByEmail(email).ifPresent(appUser -> {
                appUserRepository.deleteById(appUser.getId());
                securedUserRepository.deleteById(appUser.getSecuredUser().getId());
            });
        }
    }
}
