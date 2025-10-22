package com.arjunagi.urlshortner.repository;

import com.arjunagi.urlshortner.models.ClickAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IClickAnalyticsRepo extends MongoRepository<ClickAnalytics, String> {

  List<ClickAnalytics> findByShortUrlCodeOrderByClickedAtDesc(String shortUrlCode);

  @Query("{ 'shortUrlCode': ?0, 'clickedAt': { $gte: ?1, $lte: ?2 } }")
  List<ClickAnalytics> findByShortUrlCodeAndClickedAtBetween(String shortUrlCode, LocalDateTime start,
      LocalDateTime end);

  long countByShortUrlCode(String shortUrlCode);

  // We'll calculate unique clicks differently since MongoDB distinct count is
  // complex
  @Query("{ 'shortUrlCode': ?0 }")
  List<ClickAnalytics> findByShortUrlCodeForUniqueCount(String shortUrlCode);

  boolean existsByShortUrlCodeAndSessionId(String shortUrlCode, String sessionId);
}