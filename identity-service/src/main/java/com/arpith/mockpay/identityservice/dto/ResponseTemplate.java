package com.arpith.mockpay.identityservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ResponseTemplate<T> {
    private Instant timestamp;

    private HttpStatus status;

    private String message;

    private T data;

    public ResponseTemplate() {
        this.timestamp = Instant.now();
    }

    public ResponseTemplate(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ResponseTemplate(HttpStatus status, String message, T data) {
        this(status, message);
        this.data = data;
    }
}

