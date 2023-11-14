package com.arpith.mockpay.identityservice.repository;

import com.arpith.mockpay.identityservice.constant.Constant;
import com.arpith.mockpay.identityservice.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class AppUserCacheRepository {
    private static final Logger LOG = LoggerFactory.getLogger(AppUserCacheRepository.class);
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.expire-time}")
    Long expiry;

    public AppUser get(Integer userId) {
        LOG.info("Entering AppUserCacheRepository.get");
        var result = redisTemplate.opsForValue().get(getKey(userId));
        return (result == null) ? null : (AppUser) result;
    }

    public void set(AppUser appUser) {
        LOG.info("Entering AppUserCacheRepository.set");
        redisTemplate.opsForValue().set(
                getKey(Math.toIntExact(appUser.getId())), appUser, expiry, TimeUnit.SECONDS);
    }

    private String getKey(Integer userId) {
        LOG.info("Entering AppUserCacheRepository.getKey");
        return Constant.APP_USER_CACHE_KEY_PREFIX + userId;
    }
}
