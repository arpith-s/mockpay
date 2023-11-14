package com.arpith.mockpay.transactionservice.filter;

import com.arpith.mockpay.transactionservice.constant.Constant;
import com.arpith.mockpay.transactionservice.constant.LogMessage;
import com.arpith.mockpay.transactionservice.dto.AccessToken;
import com.arpith.mockpay.transactionservice.dto.SecuredUser;
import com.arpith.mockpay.transactionservice.service.IdentityServiceClient;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final IdentityServiceClient identityServiceClient;
    private final Gson gson;

    @Value("${application.security.service.email}")
    String serviceUserEmail;

    @Value("${application.security.service.secret}")
    String serviceUserSecret;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        LOG.info("Entering JwtRequestFilter.doFilterInternal");
        final String token = extractToken(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null)
            authenticateUser(token, request);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        LOG.info("Entering JwtRequestFilter.extractToken");
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        } else {
            LOG.warn(LogMessage.INVALID_AUTH_HEADER);
            return null;
        }
    }

    private void authenticateUser(String token, HttpServletRequest request) {
        LOG.info("Entering JwtRequestFilter.authenticateUser");
        try {
            var authToken = serviceUserEmail + Constant.DELIMITER_COLON + serviceUserSecret;
            authToken = Base64.getEncoder().encodeToString(authToken.getBytes());
            var response = identityServiceClient.authenticate(
                    Map.of("Authorization", "Basic " + authToken), AccessToken.builder().token(token).build()
            );

            if (response.getStatus().equals(HttpStatus.OK)) {
                String securedUserEncoded = response.getData().get("user_details");
                String securedUserJson = new String(Base64.getDecoder().decode(securedUserEncoded));
                SecuredUser securedUser = gson.fromJson(securedUserJson, SecuredUser.class);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securedUser, null, securedUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                LOG.info(LogMessage.JWT_TOKEN_VALID_ERROR);
            }
        } catch (WebClientRequestException | WebClientResponseException e) {
            LOG.info(LogMessage.JWT_TOKEN_VALID_ERROR, e);
        }
    }
}
