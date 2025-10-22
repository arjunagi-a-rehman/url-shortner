package com.arjunagi.urlshortner.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class ClickAnalytics {
    @Id
    private String id;
    private String shortUrlCode;
    private String ipAddress;
    private String userAgent;
    private String country;
    private String region;
    private String city;
    private String timezone;
    private LocalDateTime clickedAt;
    private String referrer;
    private String sessionId; // For unique user tracking
}