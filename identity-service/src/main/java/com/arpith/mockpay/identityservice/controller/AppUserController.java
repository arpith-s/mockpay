package com.arpith.mockpay.identityservice.controller;

import com.arpith.mockpay.identityservice.constant.ResponseMessage;
import com.arpith.mockpay.identityservice.dto.CreateAppUserRequest;
import com.arpith.mockpay.identityservice.dto.ResponseTemplate;
import com.arpith.mockpay.identityservice.dto.UpdateAppUserRequest;
import com.arpith.mockpay.identityservice.exception.InvalidAuthorityException;
import com.arpith.mockpay.identityservice.exception.InvalidUserTypeException;
import com.arpith.mockpay.identityservice.exception.NonExistentAppUserException;
import com.arpith.mockpay.identityservice.exception.UniqueAppUserConstraintViolationException;
import com.arpith.mockpay.identityservice.mapper.EntityMapper;
import com.arpith.mockpay.identityservice.model.SecuredUser;
import com.arpith.mockpay.identityservice.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class AppUserController {
    private static final Logger LOG = LoggerFactory.getLogger(AppUserController.class);
    private final AppUserService appUserService;

    @PostMapping("/create")
    public ResponseEntity<ResponseTemplate<Object>> createAppUser(@RequestBody @Valid CreateAppUserRequest createAppUserRequest) throws InvalidUserTypeException, UniqueAppUserConstraintViolationException {
        LOG.info("Entering AppUserController.createAppUser");
        appUserService.create(EntityMapper.toAppUser(createAppUserRequest), createAppUserRequest.getUserType());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseTemplate<>(HttpStatus.CREATED, ResponseMessage.APP_USER_CREATED));
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseTemplate<Object>> getAppUser(@AuthenticationPrincipal SecuredUser securedUser) throws NonExistentAppUserException {
        LOG.info("Entering AppUserController.getAppUser");
        var appUserResponse = List.of(EntityMapper.toAppUserResponse(appUserService.get(securedUser)));
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, ResponseMessage.APP_USER_FETCHED, appUserResponse));
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseTemplate<Object>> updateAppUser(@RequestBody @Valid UpdateAppUserRequest updateAppUserRequest, @AuthenticationPrincipal SecuredUser securedUser) throws UniqueAppUserConstraintViolationException, IllegalAccessException, InvalidAuthorityException, NonExistentAppUserException {
        LOG.info("Entering AppUserController.updateAppUser");
        appUserService.update(EntityMapper.toAppUser(updateAppUserRequest), securedUser);
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, ResponseMessage.APP_USER_UPDATED));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseTemplate<Object>> deleteAppUser(@AuthenticationPrincipal SecuredUser securedUser) throws NonExistentAppUserException {
        LOG.info("Entering AppUserController.deleteAppUser");
        appUserService.deleteAppUserByEmail(securedUser.getUsername());
        return ResponseEntity.ok(new ResponseTemplate<>(HttpStatus.OK, ResponseMessage.APP_USER_DELETED));
    }
}
