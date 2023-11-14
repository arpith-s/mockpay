package com.arpith.mockpay.walletservice.exception;

import com.arpith.mockpay.walletservice.constant.Constant;
import com.arpith.mockpay.walletservice.constant.LogMessage;
import com.arpith.mockpay.walletservice.constant.ResponseMessage;
import com.arpith.mockpay.walletservice.dto.ResponseTemplate;
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
public class WalletServiceGlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(WalletServiceGlobalExceptionHandler.class);

    @ExceptionHandler({
            UniqueWalletConstraintViolationException.class, MethodArgumentNotValidException.class, NoHandlerFoundException.class
    })
    public ResponseEntity<ResponseTemplate<Object>> handleExceptions(Exception exception) {
        LOG.error("{} {} {}", LogMessage.EXCEPTION_OCCURRED, Constant.DELIMITER_COLON, exception.getMessage());
        var httpStatus = HttpStatus.OK;
        var message = exception.getMessage();

        if (exception instanceof UniqueWalletConstraintViolationException) {
            httpStatus = HttpStatus.CONFLICT;
        } else if (exception instanceof NoHandlerFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (exception instanceof MethodArgumentNotValidException ex) {
            httpStatus = HttpStatus.BAD_REQUEST;
            var validationErrors = ex.getBindingResult().getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse(Constant.DELIMITER_EMPTY)
                    ));
            message = ResponseMessage.FIELD_VALID_FAIL + Constant.DELIMITER_SPACE + validationErrors;
        }

        return ResponseEntity.status(httpStatus).body(new ResponseTemplate<>(httpStatus, message));
    }
}
