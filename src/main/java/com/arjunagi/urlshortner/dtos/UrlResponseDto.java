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
        name = "URL record",
        description = "Once the short url code is generated the record is created in db this schema holds the user related data from that record"
)
public class UrlResponseDto {
    @Schema(
            description = "The long URL for which you generated short url",
            example = "https://github.com/arjunagi-a-rehman/url-shortner/tree/main/src/main/java/com/arjunagi/urlshortner"
    )
    private String OriginalUrl;
    @Schema(
            description = "The short url code which belongs to url long url",
            example = "ccvbb12"
    )
    private String shortUrlCode;
    @Schema(
            description = "Expiry date you want to assign. note this is optional feild you can remove this from request in this case the expiry date will be 24hrs from creation",
            example = "2024-01-07T13:59:53"
    )
    private LocalDateTime expiryDate;
    
    @Schema(
            description = "The date and time when the short URL was created",
            example = "2024-01-07T10:30:00"
    )
    private LocalDateTime createdAt;
    
    @Schema(
            description = "Total number of clicks on this URL",
            example = "42"
    )
    private Long totalClicks;
    
    @Schema(
            description = "Number of unique users who clicked this URL",
            example = "28"
    )
    private Long uniqueClicks;
    
    @Schema(
            description = "Last time this URL was clicked",
            example = "2024-01-07T15:45:30"
    )
    private LocalDateTime lastClickedAt;
}
