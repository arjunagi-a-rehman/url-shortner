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
        name = "Click Activity",
        description = "Individual click activity information"
)
public class ClickActivityDto {
    @Schema(description = "Country where the click originated", example = "United States")
    private String country;
    
    @Schema(description = "Region/State where the click originated", example = "California")
    private String region;
    
    @Schema(description = "City where the click originated", example = "San Francisco")
    private String city;
    
    @Schema(description = "When the click occurred", example = "2024-01-07T15:45:30")
    private LocalDateTime clickedAt;
    
    @Schema(description = "Referrer URL if available", example = "https://google.com")
    private String referrer;
    
    @Schema(description = "User agent information", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    private String userAgent;
}