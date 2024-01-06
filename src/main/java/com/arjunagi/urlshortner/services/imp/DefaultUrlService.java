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
import org.springframework.scheduling.annotation.Async;
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
     * @param urlRequestDto consist original url and expiry date both will not be null
     * @return response dto consist of original url, short code and expiry date
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
     * @param shortUrl not null
     * @return url response dto consists of original url,expiry date and short code
     */
    @Override
    public UrlResponseDto getUrl(String shortUrl) {
        Url url=urlMongoRepo.findByShortUrl(shortUrl).orElseThrow(()->new ResourceNotFoundException("record","url",shortUrl));
        if(url.getExpiryDate().isBefore(LocalDateTime.now())) {
            asyncDelete(url);
            throw new ExpiredUrlException(shortUrl);
        }
        return UrlMapper.UrlToUrlResponseDto(url,new UrlResponseDto());
    }

    /**
     * @param urlRequestDto to update original url and expiry date , dto can be null, and in dto any field can be full
     * @param shortUrlCode as a search parameter
     * @return true if updated successfully
     */
    @Override
    public boolean updateUrl(UrlRequestDto urlRequestDto, String shortUrlCode) {
        Url url=urlMongoRepo.findByShortUrl(shortUrlCode).orElseThrow(()->new ResourceNotFoundException("record","url",shortUrlCode));
        if(urlRequestDto==null){
            url.setExpiryDate(getExpiryTime(null));
        }else{
            if(urlRequestDto.getUrl()!=null)url.setOriginalUrl(urlRequestDto.getUrl());
            url.setExpiryDate(getExpiryTime(urlRequestDto.getExpiryDate()));
        }
        urlMongoRepo.save(url);
        return true;
    }

    /**
     * @param shortUrl not null
     * @return true if deleted successfully
     */
    @Override
    public boolean deleteUrl(String shortUrl) {
        Url url=urlMongoRepo.findByShortUrl(shortUrl).orElseThrow(()->new ResourceNotFoundException("url","short url",shortUrl));
        urlMongoRepo.delete(url);
        return true;
    }

    @Async
    public void asyncDelete(Url url){
        urlMongoRepo.delete(url);
    }

    private LocalDateTime getExpiryTime(LocalDateTime dateTime){
        if(dateTime==null){  // provide next day date
            return LocalDateTime.now().plusDays(1);
        }
        if(dateTime.compareTo(LocalDateTime.now())<=0)return LocalDateTime.now().plusDays(1); // if input time stamp invalid then provide next days date
        return dateTime;
    }

    private String encodeUrl(String url) // encoding the original url to get short url code, the algo used is murmur3 32 byte encoding
    {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(url.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return  encodedUrl;
    }
}
