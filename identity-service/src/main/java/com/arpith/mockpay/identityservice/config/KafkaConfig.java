package com.arpith.mockpay.identityservice.config;

import com.arpith.mockpay.identityservice.constant.Topic;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topic.partitions}")
    public Integer partitions;
    @Value("${spring.kafka.topic.replicas}")
    public Integer replicas;
    @Value("${spring.kafka.topic.compression-type}")
    public String compressionType;

    @Bean
    public NewTopic topicAppUserCreated() {
        return TopicBuilder.name(Topic.APP_USER_CREATED)
                .partitions(partitions)
                .replicas(replicas)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, compressionType)
                .build();
    }

    @Bean
    public NewTopic topicTransactionCreated() {
        return TopicBuilder.name(Topic.TRANSACTION_CREATED)
                .partitions(partitions)
                .replicas(replicas)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, compressionType)
                .build();
    }

    @Bean
    public NewTopic topicWalletUpdated() {
        return TopicBuilder.name(Topic.WALLET_UPDATED)
                .partitions(partitions)
                .replicas(replicas)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, compressionType)
                .build();
    }


    @Bean
    public NewTopic topicTransaction() {
        return TopicBuilder.name(Topic.TRANSACTION_COMPLETED)
                .partitions(partitions)
                .replicas(replicas)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, compressionType)
                .build();
    }

    @Bean
    public NewTopic topicWalletCreated() {
        return TopicBuilder.name(Topic.WALLET_CREATED)
                .partitions(partitions)
                .replicas(replicas)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, compressionType)
                .build();
    }

    @Bean
    public NewTopic topicAppUserDeleted() {
        return TopicBuilder.name(Topic.APP_USER_DELETED)
                .partitions(partitions)
                .replicas(replicas)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, compressionType)
                .build();
    }

    @Bean
    public NewTopic topicWalletDeleted() {
        return TopicBuilder.name(Topic.WALLET_DELETED)
                .partitions(partitions)
                .replicas(replicas)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, compressionType)
                .build();
    }
}
