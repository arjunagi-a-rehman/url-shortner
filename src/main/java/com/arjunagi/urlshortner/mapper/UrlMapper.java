package com.arjunagi.urlshortner.mapper;

import com.arjunagi.urlshortner.dtos.UrlRequestDto;
import com.arjunagi.urlshortner.dtos.UrlResponseDto;
import com.arjunagi.urlshortner.models.Url;

public class UrlMapper {
    public static UrlResponseDto UrlToUrlResponseDto(Url url,UrlResponseDto urlResponseDto){
        urlResponseDto.setOriginalUrl(url.getOriginalUrl());
        urlResponseDto.setShortUrlCode(url.getShortUrl());
        urlResponseDto.setExpiryDate(url.getExpiryDate());
        return urlResponseDto;
    }
}
