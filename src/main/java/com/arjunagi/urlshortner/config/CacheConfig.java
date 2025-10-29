package com.arjunagi.urlshortner.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configure cache for all use cases - optimized for 1GB RAM
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(200)
                .maximumSize(2000)  // Reasonable size for 1GB RAM
                .expireAfterWrite(60, TimeUnit.MINUTES)  // 1 hour cache
                .expireAfterAccess(30, TimeUnit.MINUTES)  // Refresh on access
                .recordStats());
        
        // Pre-register cache names
        cacheManager.setCacheNames(java.util.Arrays.asList(
            "urlCache", 
            "analyticsCache", 
            "redirectCache", 
            "urlValidityCache"
        ));
        
        return cacheManager;
    }
}