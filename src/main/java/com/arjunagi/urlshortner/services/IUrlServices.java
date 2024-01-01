package com.arjunagi.urlshortner.services;

import com.arjunagi.urlshortner.dtos.UrlRequestDto;
import com.arjunagi.urlshortner.dtos.UrlResponseDto;
import com.arjunagi.urlshortner.models.Url;

public interface IUrlServices {
    public UrlResponseDto generateShortUrl(UrlRequestDto urlRequestDto);
    public String getUrl(String shortUrl);

    boolean updateUrl(UrlRequestDto urlRequestDto);

    boolean deleteUrl(String shortUrl);
}
