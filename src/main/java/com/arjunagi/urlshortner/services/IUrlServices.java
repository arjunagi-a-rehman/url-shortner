package com.arjunagi.urlshortner.services;

import com.arjunagi.urlshortner.dtos.UrlRequestDto;
import com.arjunagi.urlshortner.dtos.UrlResponseDto;

public interface IUrlServices {
    public UrlResponseDto generateShortUrl(UrlRequestDto urlRequestDto);
    public UrlResponseDto getUrl(String shortUrl);

    boolean updateUrl(UrlRequestDto urlRequestDto, String shortUrlCode);

    boolean deleteUrl(String shortUrl);
}
