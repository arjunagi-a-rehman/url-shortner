package com.arjunagi.urlshortner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpiredUrlException extends RuntimeException{
    public ExpiredUrlException(String shortUrl){
        super(shortUrl+" has been expired please re-create the short url");
    }
}
