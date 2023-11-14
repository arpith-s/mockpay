package com.arpith.mockpay.identityservice.utils;

import com.arpith.mockpay.identityservice.service.SecuredUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JwtUtil.class);
    private final SecuredUserService securedUserService;

    @Value("${application.security.jwt.secret-key}")
    String jwtSecretKey;

    @Value("${application.security.jwt.expire-time-ms}")
    Long jwtTokenExpireTime;

    public String generateJwtToken(UserDetails userDetails) {
        LOG.info("Entering JwtUtil.generateJwtToken");
        return createJwtToken(Map.of(), userDetails.getUsername());
    }

    private String createJwtToken(Map<String, Object> claims, String subject) {
        LOG.info("Entering JwtUtil.createJwtToken");
        final var nowUtc = Instant.now();
        final var expirationTime = nowUtc.plusMillis(jwtTokenExpireTime);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(Date.from(nowUtc))
                .setExpiration(Date.from(expirationTime))
                .signWith(getJwtTokenSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getJwtTokenSigningKey() {
        LOG.info("Entering JwtUtil.getJwtTokenSigningKey");
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Boolean validateJwtToken(String jwtToken) {
        LOG.info("Entering JwtUtil.validateJwtToken");
        try {
            final var username = extractJwtTokenUsername(jwtToken);
            return !isJwtTokenExpired(jwtToken) && securedUserService.loadUserByUsername(username) != null;
        } catch (Exception e) {
            LOG.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String extractJwtTokenUsername(String jwtToken) {
        LOG.info("Entering JwtUtil.extractJwtTokenUsername");
        return extractJwtTokenClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractJwtTokenClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        LOG.info("Entering JwtUtil.extractJwtTokenClaim");
        final var claims = extractAllJwtTokenClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllJwtTokenClaims(String jwtToken) {
        LOG.info("Entering JwtUtil.extractAllJwtTokenClaims");
        return Jwts.parserBuilder()
                .setSigningKey(getJwtTokenSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Boolean isJwtTokenExpired(String jwtToken) {
        LOG.info("Entering JwtUtil.isJwtTokenExpired");
        return extractJwtTokenExpiration(jwtToken).before(new Date());
    }

    private Date extractJwtTokenExpiration(String jwtToken) {
        LOG.info("Entering JwtUtil.extractJwtTokenExpiration");
        return extractJwtTokenClaim(jwtToken, Claims::getExpiration);
    }
}
