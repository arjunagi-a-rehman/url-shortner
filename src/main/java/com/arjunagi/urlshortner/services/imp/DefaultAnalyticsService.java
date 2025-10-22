package com.arjunagi.urlshortner.services.imp;

import com.arjunagi.urlshortner.dtos.AnalyticsResponseDto;
import com.arjunagi.urlshortner.dtos.ClickActivityDto;
import com.arjunagi.urlshortner.models.ClickAnalytics;
import com.arjunagi.urlshortner.models.Url;
import com.arjunagi.urlshortner.repository.IClickAnalyticsRepo;
import com.arjunagi.urlshortner.repository.IUrlMongoRepo;
import com.arjunagi.urlshortner.services.GeoLocationService;
import com.arjunagi.urlshortner.services.IAnalyticsService;
import com.arjunagi.urlshortner.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DefaultAnalyticsService implements IAnalyticsService {
    
    @Autowired
    private IClickAnalyticsRepo clickAnalyticsRepo;
    
    @Autowired
    private IUrlMongoRepo urlMongoRepo;
    
    @Autowired
    private GeoLocationService geoLocationService;
    
    @Override
    @Async
    public void recordClick(String shortUrlCode, HttpServletRequest request, String sessionId) {
        try {
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            String referrer = request.getHeader("Referer");
            
            // Get location info
            GeoLocationService.GeoLocationInfo locationInfo = geoLocationService.getLocationInfo(ipAddress);
            
            // Create click analytics record
            ClickAnalytics clickAnalytics = new ClickAnalytics();
            clickAnalytics.setShortUrlCode(shortUrlCode);
            clickAnalytics.setIpAddress(ipAddress);
            clickAnalytics.setUserAgent(userAgent);
            clickAnalytics.setCountry(locationInfo.country);
            clickAnalytics.setRegion(locationInfo.region);
            clickAnalytics.setCity(locationInfo.city);
            clickAnalytics.setTimezone(locationInfo.timezone);
            clickAnalytics.setClickedAt(LocalDateTime.now());
            clickAnalytics.setReferrer(referrer);
            clickAnalytics.setSessionId(sessionId);
            
            // Update URL statistics BEFORE saving click analytics
            updateUrlStatistics(shortUrlCode, sessionId);
            
            // Save click analytics record
            clickAnalyticsRepo.save(clickAnalytics);
            
        } catch (Exception e) {
            log.error("Error recording click for shortUrlCode: {}, error: {}", shortUrlCode, e.getMessage());
        }
    }
    
    @Override
    public AnalyticsResponseDto getAnalytics(String shortUrlCode) {
        // Get URL info
        Url url = urlMongoRepo.findByShortUrl(shortUrlCode)
            .orElseThrow(() -> new ResourceNotFoundException("record", "url", shortUrlCode));
        
        // Get all clicks for this URL
        List<ClickAnalytics> allClicks = clickAnalyticsRepo.findByShortUrlCodeOrderByClickedAtDesc(shortUrlCode);
        
        // Build analytics response
        AnalyticsResponseDto analytics = new AnalyticsResponseDto();
        analytics.setShortUrlCode(shortUrlCode);
        analytics.setOriginalUrl(url.getOriginalUrl());
        analytics.setTotalClicks(url.getTotalClicks());
        analytics.setUniqueClicks(url.getUniqueClicks());
        analytics.setCreatedAt(url.getCreatedAt());
        analytics.setExpiryDate(url.getExpiryDate());
        analytics.setLastClickedAt(url.getLastClickedAt());
        
        // Calculate geographical distribution
        analytics.setClicksByCountry(calculateClicksByCountry(allClicks));
        analytics.setClicksByRegion(calculateClicksByRegion(allClicks));
        
        // Get recent clicks (last 10)
        analytics.setRecentClicks(getRecentClickActivities(allClicks, 10));
        
        // Calculate daily stats for last 30 days
        analytics.setDailyClickStats(calculateDailyStats(allClicks, 30));
        
        return analytics;
    }
    
    @Override
    public boolean isUniqueClick(String shortUrlCode, String sessionId) {
        return !clickAnalyticsRepo.existsByShortUrlCodeAndSessionId(shortUrlCode, sessionId);
    }
    
    private void updateUrlStatistics(String shortUrlCode, String sessionId) {
        Optional<Url> urlOpt = urlMongoRepo.findByShortUrl(shortUrlCode);
        if (urlOpt.isPresent()) {
            Url url = urlOpt.get();
            
            // Check if this is a unique click BEFORE saving the analytics record
            boolean isUnique = !clickAnalyticsRepo.existsByShortUrlCodeAndSessionId(shortUrlCode, sessionId);
            
            // Increment total clicks
            url.setTotalClicks(url.getTotalClicks() + 1);
            
            // Increment unique clicks if this is a unique session
            if (isUnique) {
                url.setUniqueClicks(url.getUniqueClicks() + 1);
            }
            
            // Update last clicked time
            url.setLastClickedAt(LocalDateTime.now());
            
            urlMongoRepo.save(url);
        }
    }
    
    private Map<String, Long> calculateClicksByCountry(List<ClickAnalytics> clicks) {
        return clicks.stream()
            .collect(Collectors.groupingBy(
                click -> click.getCountry() != null ? click.getCountry() : "Unknown",
                Collectors.counting()
            ));
    }
    
    private Map<String, Long> calculateClicksByRegion(List<ClickAnalytics> clicks) {
        return clicks.stream()
            .collect(Collectors.groupingBy(
                click -> {
                    String country = click.getCountry() != null ? click.getCountry() : "Unknown";
                    String region = click.getRegion() != null ? click.getRegion() : "Unknown";
                    return country + " - " + region;
                },
                Collectors.counting()
            ));
    }
    
    private List<ClickActivityDto> getRecentClickActivities(List<ClickAnalytics> clicks, int limit) {
        return clicks.stream()
            .limit(limit)
            .map(click -> new ClickActivityDto(
                click.getCountry(),
                click.getRegion(),
                click.getCity(),
                click.getClickedAt(),
                click.getReferrer(),
                click.getUserAgent()
            ))
            .collect(Collectors.toList());
    }
    
    private Map<String, Long> calculateDailyStats(List<ClickAnalytics> clicks, int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return clicks.stream()
            .filter(click -> click.getClickedAt().isAfter(cutoff))
            .collect(Collectors.groupingBy(
                click -> click.getClickedAt().format(formatter),
                Collectors.counting()
            ));
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}