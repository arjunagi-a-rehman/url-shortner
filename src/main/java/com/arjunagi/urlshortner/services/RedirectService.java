package com.arjunagi.urlshortner.services;

import com.arjunagi.urlshortner.exceptions.ExpiredUrlException;
import com.arjunagi.urlshortner.exceptions.ResourceNotFoundException;
import com.arjunagi.urlshortner.models.Url;
import com.arjunagi.urlshortner.repository.IUrlMongoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RedirectService {
    
    @Autowired
    private IUrlMongoRepo urlMongoRepo;
    
    /**
     * High-performance redirect lookup with aggressive caching
     * This method is optimized for the most frequent operation - URL redirects
     */
    @Cacheable(value = "redirectCache", key = "#shortUrlCode", unless = "#result == null")
    public String getOriginalUrl(String shortUrlCode) {
        log.debug("Cache miss for redirect: {}", shortUrlCode);
        
        Url url = urlMongoRepo.findByShortUrl(shortUrlCode)
            .orElseThrow(() -> new ResourceNotFoundException("record", "url", shortUrlCode));
        
        // Check expiry
        if (url.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ExpiredUrlException(shortUrlCode);
        }
        
        return url.getOriginalUrl();
    }
    
    /**
     * Check if URL exists and is valid (cached)
     */
    @Cacheable(value = "urlValidityCache", key = "#shortUrlCode")
    public boolean isValidUrl(String shortUrlCode) {
        try {
            Url url = urlMongoRepo.findByShortUrl(shortUrlCode)
                .orElse(null);
            
            if (url == null) {
                return false;
            }
            
            return url.getExpiryDate().isAfter(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error checking URL validity for {}: {}", shortUrlCode, e.getMessage());
            return false;
        }
    }
}