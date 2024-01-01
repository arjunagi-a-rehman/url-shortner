package com.arjunagi.urlshortner.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlResponseDto {
    private String OriginalUrl;
    private String shortUrl;
    private LocalDateTime expiryDate;
}
