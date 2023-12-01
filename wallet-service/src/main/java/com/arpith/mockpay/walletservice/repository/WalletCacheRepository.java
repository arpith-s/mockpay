package com.arpith.mockpay.walletservice.repository;

import com.arpith.mockpay.walletservice.constant.Constant;
import com.arpith.mockpay.walletservice.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class WalletCacheRepository {
    private static final Logger LOG = LoggerFactory.getLogger(WalletCacheRepository.class);
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.expire-time}")
    private Long expiry;

    public Wallet get(String walletId) {
        LOG.info("Entering WalletCacheRepository.get");
        var result = redisTemplate.opsForValue().get(getKey(walletId));
        return (result == null) ? null : (Wallet) result;
    }

    public void set(String senderId, Wallet wallet) {
        LOG.info("Entering WalletCacheRepository.set");
        redisTemplate.opsForValue().set(getKey(senderId), wallet, expiry, TimeUnit.SECONDS);
    }

    private String getKey(String senderId) {
        LOG.info("Entering WalletCacheRepository.getKey");
        return Constant.WALLET_CACHE_KEY_PREFIX + senderId;
    }

    public void invalidateCache(String walletId) {
        LOG.info("Invalidating cache for walletId: {}", walletId);
        redisTemplate.delete(getKey(walletId));
    }
}
