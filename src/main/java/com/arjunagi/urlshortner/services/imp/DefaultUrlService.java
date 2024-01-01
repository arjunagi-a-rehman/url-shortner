package com.arjunagi.urlshortner.services.imp;

import com.arjunagi.urlshortner.dtos.UrlRequestDto;
import com.arjunagi.urlshortner.dtos.UrlResponseDto;
import com.arjunagi.urlshortner.exceptions.ExpiredUrlException;
import com.arjunagi.urlshortner.exceptions.ResourceNotFoundException;
import com.arjunagi.urlshortner.mapper.UrlMapper;
import com.arjunagi.urlshortner.models.Url;
import com.arjunagi.urlshortner.repository.IUrlMongoRepo;
import com.arjunagi.urlshortner.services.IUrlServices;
import com.arjunagi.urlshortner.services.NextSequenceService;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class DefaultUrlService implements IUrlServices {
    @Autowired
    IUrlMongoRepo urlMongoRepo;
    @Autowired
    private NextSequenceService nextSequenceService;

    /**
     * @param urlRequestDto
     * @return
     */
    @Override
    public UrlResponseDto generateShortUrl(UrlRequestDto urlRequestDto) {
        Url url=new Url();
        url.setId(String.valueOf(nextSequenceService.getNextSequence("customSequences")));
        url.setOriginalUrl(urlRequestDto.getUrl());
        url.setShortUrl(encodeUrl(urlRequestDto.getUrl()));
        url.setExpiryDate(getExpiryTime(urlRequestDto.getExpiryDate()));
        url.setCreatedAt(LocalDateTime.now());
        urlMongoRepo.save(url);
        return UrlMapper.UrlToUrlResponseDto(url,new UrlResponseDto());
    }

    /**
     * @param shortUrl
     * @return
     */
    @Override
    public String getUrl(String shortUrl) {
        Url url=urlMongoRepo.findByShortUrl(shortUrl).orElseThrow(()->new ResourceNotFoundException("record","url",shortUrl));
        if(url.getExpiryDate().isBefore(LocalDateTime.now()))throw new ExpiredUrlException(shortUrl);
        return url.getOriginalUrl();
    }

    /**
     * @param urlRequestDto
     * @return
     */
    @Override
    public boolean updateUrl(UrlRequestDto urlRequestDto) {
        Url url=urlMongoRepo.findByOriginalUrl(urlRequestDto.getUrl()).orElseThrow(()->new ResourceNotFoundException("record","url",urlRequestDto.getUrl()));
        url.setExpiryDate(getExpiryTime(urlRequestDto.getExpiryDate()));
        urlMongoRepo.save(url);
        return true;
    }

    /**
     * @param shortUrl
     * @return
     */
    @Override
    public boolean deleteUrl(String shortUrl) {
        Url url=urlMongoRepo.findByShortUrl(shortUrl).orElseThrow(()->new ResourceNotFoundException("url","short url",shortUrl));
        urlMongoRepo.delete(url);
        return true;
    }

    private LocalDateTime getExpiryTime(LocalDateTime dateTime){
        if(dateTime==null){
            return LocalDateTime.now().plusHours(1);
        }
        if(dateTime.compareTo(LocalDateTime.now())<=0)return LocalDateTime.now().plusHours(1);
        return dateTime;
    }

    private String encodeUrl(String url)
    {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return  encodedUrl;
    }
}
