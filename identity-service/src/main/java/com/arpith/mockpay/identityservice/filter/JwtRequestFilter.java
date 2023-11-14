package com.arpith.mockpay.identityservice.filter;

import com.arpith.mockpay.identityservice.constant.LogMessage;
import com.arpith.mockpay.identityservice.service.SecuredUserService;
import com.arpith.mockpay.identityservice.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtRequestFilter.class);
    private final JwtUtil jwtUtil;
    private final SecuredUserService securedUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        LOG.info("Entering JwtRequestFilter.doFilterInternal");
        final String token = extractToken(request);
        String username = validateTokenAndGetUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            authenticateUser(username, token, request);

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

    private String validateTokenAndGetUsername(String token) {
        LOG.info("Entering JwtRequestFilter.validateTokenAndGetUsername");
        if (token == null) return null;
        try {
            return jwtUtil.extractJwtTokenUsername(token);
        } catch (IllegalArgumentException e) {
            LOG.error(LogMessage.ILLEGAL_ARGUMENT, e);
        } catch (ExpiredJwtException e) {
            LOG.warn(LogMessage.EXPIRED_JWT, e);
        } catch (MalformedJwtException e) {
            LOG.error(LogMessage.MALFORMED_JWT, e);
        } catch (Exception e) {
            LOG.error(LogMessage.JWT_TOKEN_VALID_ERROR, e);
        }
        return null;
    }

    private void authenticateUser(String username, String token, HttpServletRequest request) {
        LOG.info("Entering JwtRequestFilter.authenticateUser");
        var userDetails = securedUserService.loadUserByUsername(username);
        if (Boolean.TRUE.equals(jwtUtil.validateJwtToken(token))) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            LOG.info(LogMessage.JWT_TOKEN_VALID_ERROR);
        }
    }
}
