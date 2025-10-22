package com.arjunagi.urlshortner.services;

import com.arjunagi.urlshortner.dtos.AnalyticsResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IAnalyticsService {
    
    /**
     * Records a click event for analytics tracking
     * @param shortUrlCode the short URL code that was clicked
     * @param request HTTP request to extract IP, user agent, etc.
     * @param sessionId unique session identifier for tracking unique users
     */
    void recordClick(String shortUrlCode, HttpServletRequest request, String sessionId);
    
    /**
     * Gets comprehensive analytics for a short URL
     * @param shortUrlCode the short URL code to get analytics for
     * @return detailed analytics including click counts, geography, and recent activity
     */
    AnalyticsResponseDto getAnalytics(String shortUrlCode);
    
    /**
     * Checks if this is a unique click (new session for this URL)
     * @param shortUrlCode the short URL code
     * @param sessionId the session identifier
     * @return true if this is a unique click
     */
    boolean isUniqueClick(String shortUrlCode, String sessionId);
}