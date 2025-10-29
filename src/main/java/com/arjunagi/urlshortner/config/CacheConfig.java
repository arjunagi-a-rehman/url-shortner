package com.arjunagi.urlshortner.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Configure different cache policies for different use cases
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)  // Limit cache size for 1GB RAM
                .expireAfterWrite(30, TimeUnit.MINUTES)  // URLs cached for 30 minutes
                .recordStats());  // Enable cache statistics
        
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats();
    }

    // Specific cache for URL lookups (most frequent operation)
    @Bean
    public CacheManager urlCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("urlCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(200)
                .maximumSize(2000)  // More space for URL lookups
                .expireAfterWrite(60, TimeUnit.MINUTES)  // Longer cache for URLs
                .expireAfterAccess(30, TimeUnit.MINUTES)  // Refresh on access
                .recordStats());
        return cacheManager;
    }

    // Cache for analytics (less frequent, can be cached longer)
    @Bean
    public CacheManager analyticsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("analyticsCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)  // Analytics can be slightly stale
                .recordStats());
        return cacheManager;
    }

    // High-performance cache for redirects (most frequent operation)
    @Bean
    public CacheManager redirectCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("redirectCache", "urlValidityCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(500)
                .maximumSize(5000)  // Large cache for popular URLs
                .expireAfterWrite(2, TimeUnit.HOURS)  // Long cache for redirects
                .expireAfterAccess(1, TimeUnit.HOURS)  // Refresh frequently accessed URLs
                .recordStats());
        return cacheManager;
    }
}