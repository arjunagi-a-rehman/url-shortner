package com.arjunagi.urlshortner.repository;

import com.arjunagi.urlshortner.models.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUrlMongoRepo extends MongoRepository<Url,String> {
    Optional<Url> findByOriginalUrl(String url);
    Optional<Url> findByShortUrl(String url);
}
