package com.arjunagi.urlshortner.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Analytics Response",
        description = "Comprehensive analytics data for a short URL including click statistics and geographical distribution"
)
public class AnalyticsResponseDto {
    @Schema(description = "Short URL code", example = "ccvbb12")
    private String shortUrlCode;
    
    @Schema(description = "Original URL", example = "https://github.com/example")
    private String originalUrl;
    
    @Schema(description = "Total number of clicks", example = "150")
    private Long totalClicks;
    
    @Schema(description = "Number of unique users/clicks", example = "89")
    private Long uniqueClicks;
    
    @Schema(description = "When the URL was created", example = "2024-01-07T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "When the URL expires", example = "2024-01-08T10:30:00")
    private LocalDateTime expiryDate;
    
    @Schema(description = "Last time the URL was clicked", example = "2024-01-07T15:45:30")
    private LocalDateTime lastClickedAt;
    
    @Schema(description = "Click distribution by country")
    private Map<String, Long> clicksByCountry;
    
    @Schema(description = "Click distribution by region")
    private Map<String, Long> clicksByRegion;
    
    @Schema(description = "Recent click activities")
    private List<ClickActivityDto> recentClicks;
    
    @Schema(description = "Daily click statistics for the last 30 days")
    private Map<String, Long> dailyClickStats;
}