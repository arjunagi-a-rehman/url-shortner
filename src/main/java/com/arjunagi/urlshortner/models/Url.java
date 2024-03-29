package com.arjunagi.urlshortner.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
public class Url {
    @Id
    private String id;
    private String originalUrl;
    @Indexed(unique = true)
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiryDate;
}
