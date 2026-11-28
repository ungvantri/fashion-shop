package com.fashionshop.fashion_shop.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("variants", defaultConfig.entryTtl(Duration.ofHours(6)))
                .withCacheConfiguration("brands", defaultConfig.entryTtl(Duration.ofDays(1)))
                .withCacheConfiguration("categories", defaultConfig.entryTtl(Duration.ofDays(1)))
                .withCacheConfiguration("stores", defaultConfig.entryTtl(Duration.ofDays(1)))
                .withCacheConfiguration("customer_tiers", defaultConfig.entryTtl(Duration.ofDays(1)))
                .withCacheConfiguration("customers", defaultConfig.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("users", defaultConfig.entryTtl(Duration.ofHours(2)))
                .withCacheConfiguration("products", defaultConfig.entryTtl(Duration.ofHours(2)))
                .withCacheConfiguration("vouchers", defaultConfig.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("orders", defaultConfig.entryTtl(Duration.ofHours(1)))
                .withCacheConfiguration("stock_transfers", defaultConfig.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("reports", defaultConfig.entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("inventories", defaultConfig.entryTtl(Duration.ofMinutes(5)))
                .build();
    }
}
