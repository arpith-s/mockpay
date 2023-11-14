package com.arpith.mockpay.identityservice.exception;

import com.arpith.mockpay.identityservice.constant.Constant;
import com.arpith.mockpay.identityservice.constant.LogMessage;
import com.arpith.mockpay.identityservice.constant.ResponseMessage;
import com.arpith.mockpay.identityservice.dto.ResponseTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class AuthenticationServiceGlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceGlobalExceptionHandler.class);

    @ExceptionHandler({
            UniqueAppUserConstraintViolationException.class, InvalidAuthorityException.class, NonExistentAppUserException.class,
            JwtTokenValidationException.class, InvalidUserTypeException.class, MethodArgumentNotValidException.class,
            NoHandlerFoundException.class
    })
    public ResponseEntity<ResponseTemplate<Object>> handleExceptions(Exception exception) {
        LOG.error("{} {} {}", LogMessage.EXCEPTION_OCCURRED, Constant.DELIMITER_COLON, exception.getMessage());
        var responseStatus = HttpStatus.OK;
        var payloadStatus = HttpStatus.OK;
        var message = exception.getMessage();

        if (exception instanceof UniqueAppUserConstraintViolationException) {
            responseStatus = payloadStatus = HttpStatus.CONFLICT;
        } else if (exception instanceof InvalidAuthorityException || exception instanceof InvalidUserTypeException) {
            responseStatus = payloadStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (exception instanceof NonExistentAppUserException || exception instanceof NoHandlerFoundException) {
            responseStatus = payloadStatus = HttpStatus.NOT_FOUND;
        } else if (exception instanceof JwtTokenValidationException) {
            payloadStatus = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof MethodArgumentNotValidException ex) {
            responseStatus = payloadStatus = HttpStatus.BAD_REQUEST;
            var validationErrors = ex.getBindingResult().getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse(Constant.DELIMITER_EMPTY)
                    ));
            message = ResponseMessage.FIELD_VALID_FAIL + Constant.DELIMITER_SPACE + validationErrors;
        }

        return ResponseEntity.status(responseStatus).body(new ResponseTemplate<>(payloadStatus, message));
    }
}
