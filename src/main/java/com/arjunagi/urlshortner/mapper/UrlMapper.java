package com.arjunagi.urlshortner.mapper;

import com.arjunagi.urlshortner.dtos.UrlResponseDto;
import com.arjunagi.urlshortner.models.Url;

public class UrlMapper {
    public static UrlResponseDto UrlToUrlResponseDto(Url url, UrlResponseDto urlResponseDto){
        urlResponseDto.setOriginalUrl(url.getOriginalUrl());
        urlResponseDto.setShortUrlCode(url.getShortUrl());
        urlResponseDto.setExpiryDate(url.getExpiryDate());
        urlResponseDto.setCreatedAt(url.getCreatedAt());
        urlResponseDto.setTotalClicks(url.getTotalClicks());
        urlResponseDto.setUniqueClicks(url.getUniqueClicks());
        urlResponseDto.setLastClickedAt(url.getLastClickedAt());
        return urlResponseDto;
    }
}
