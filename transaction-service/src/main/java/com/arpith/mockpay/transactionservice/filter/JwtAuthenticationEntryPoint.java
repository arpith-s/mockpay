package com.arpith.mockpay.transactionservice.filter;

import com.arpith.mockpay.transactionservice.constant.Constant;
import com.arpith.mockpay.transactionservice.constant.LogMessage;
import com.arpith.mockpay.transactionservice.dto.ResponseTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        LOG.info("Entering JwtAuthenticationEntryPoint.commence");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ResponseTemplate<Void> responseTemplate = new ResponseTemplate<>(
                HttpStatus.UNAUTHORIZED,
                LogMessage.ACCESS_DENIED + Constant.DELIMITER_COLON + Constant.DELIMITER_SPACE + authException.getMessage()
        );
        String jsonResponse = mapper.writeValueAsString(responseTemplate);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(jsonResponse);
            writer.flush();
        }
    }
}
