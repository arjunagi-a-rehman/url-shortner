package com.arjunagi.urlshortner.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
@Slf4j
public class GeoLocationService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public GeoLocationInfo getLocationInfo(String ipAddress) {
        // Skip location lookup for localhost/private IPs
        if (isLocalOrPrivateIP(ipAddress)) {
            return new GeoLocationInfo("Unknown", "Unknown", "Unknown", "Unknown");
        }
        
        try {
            // Using ip-api.com free service (100 requests per minute limit)
            String url = "http://ip-api.com/json/" + ipAddress + "?fields=status,country,regionName,city,timezone";
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && "success".equals(response.get("status"))) {
                return new GeoLocationInfo(
                    (String) response.getOrDefault("country", "Unknown"),
                    (String) response.getOrDefault("regionName", "Unknown"),
                    (String) response.getOrDefault("city", "Unknown"),
                    (String) response.getOrDefault("timezone", "Unknown")
                );
            }
        } catch (RestClientException e) {
            log.warn("Failed to get location info for IP: {}, error: {}", ipAddress, e.getMessage());
        }
        
        return new GeoLocationInfo("Unknown", "Unknown", "Unknown", "Unknown");
    }
    
    private boolean isLocalOrPrivateIP(String ip) {
        return ip == null || 
               ip.equals("127.0.0.1") || 
               ip.equals("0:0:0:0:0:0:0:1") || 
               ip.equals("::1") ||
               ip.startsWith("192.168.") ||
               ip.startsWith("10.") ||
               ip.startsWith("172.");
    }
    
    public static class GeoLocationInfo {
        public final String country;
        public final String region;
        public final String city;
        public final String timezone;
        
        public GeoLocationInfo(String country, String region, String city, String timezone) {
            this.country = country;
            this.region = region;
            this.city = city;
            this.timezone = timezone;
        }
    }
}