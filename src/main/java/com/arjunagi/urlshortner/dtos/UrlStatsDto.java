package com.arjunagi.urlshortner.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "URL Statistics",
        description = "Statistics and metadata for a shortened URL"
)
public class UrlStatsDto {
    
    @Schema(description = "Original long URL")
    private String originalUrl;
    
    @Schema(description = "Short URL code")
    private String shortUrlCode;
    
    @Schema(description = "Full short URL")
    private String fullShortUrl;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Expiry timestamp")
    private LocalDateTime expiryDate;
    
    @Schema(description = "Days until expiry")
    private long daysUntilExpiry;
    
    @Schema(description = "Whether URL is active")
    private boolean isActive;
}