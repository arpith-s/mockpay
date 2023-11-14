package com.arpith.mockpay.transactionservice.repository;

import com.arpith.mockpay.transactionservice.config.Constant;
import com.arpith.mockpay.transactionservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TransactionCacheRepository {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionCacheRepository.class);
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.expire-time}")
    private Long expiry;

    public List<Transaction> get(String senderId) {
        LOG.info("Entering TransactionCacheRepository.get");
        var result = redisTemplate.opsForValue().get(getKey(senderId));
        return (result == null) ? null : castToListOfTransactions(result);
    }

    public void set(String senderId, List<Transaction> transactions) {
        LOG.info("Entering TransactionCacheRepository.set");
        redisTemplate.opsForValue().set(
                getKey(senderId), transactions, expiry, TimeUnit.SECONDS);
    }

    private String getKey(String senderId) {
        LOG.info("Entering TransactionCacheRepository.getKey");
        return Constant.TRANSACTION_CACHE_KEY_PREFIX + senderId;
    }

    @SuppressWarnings("unchecked")
    private List<Transaction> castToListOfTransactions(Object obj) {
        LOG.info("Entering TransactionCacheRepository.castToListOfTransactions");
        try {
            return (List<Transaction>) obj;
        } catch (ClassCastException e) {
            LOG.error("Error casting to List<Transaction>", e);
            return Collections.emptyList();
        }
    }

    public void invalidateCache(String senderId) {
        LOG.info("Invalidating cache for senderId: {}", senderId);
        redisTemplate.delete(getKey(senderId));
    }
}
