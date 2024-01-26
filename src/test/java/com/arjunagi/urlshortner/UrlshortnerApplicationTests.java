package com.arjunagi.urlshortner;

import com.arjunagi.urlshortner.dtos.UrlRequestDto;
import com.arjunagi.urlshortner.dtos.UrlResponseDto;
import com.arjunagi.urlshortner.exceptions.ExpiredUrlException;
import com.arjunagi.urlshortner.exceptions.ResourceNotFoundException;
import com.arjunagi.urlshortner.models.Url;
import com.arjunagi.urlshortner.repository.IUrlMongoRepo;
import com.arjunagi.urlshortner.services.NextSequenceService;
import com.arjunagi.urlshortner.services.imp.DefaultUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//class UrlshortnerApplicationTests {
//
//	@MockBean
//	IUrlMongoRepo repo;
//
//	@MockBean
//	private NextSequenceService sequenceService;
//
//	@InjectMocks
//	private DefaultUrlService service;
//
//	private static String shortUrl = "ac123gh";
//	private static String longUrl = "https://exmple.com/example";
//	private static LocalDateTime dateTime = LocalDateTime.now();
//
//
//
//	@Test
//	@DisplayName("generate short url from long using UrlRequestDto as inp and URLResponseDto as out")
//	void generateShortUrlFromDtoTest() {
//		UrlResponseDto actualResponse = service.generateShortUrl(new UrlRequestDto(longUrl, dateTime));
//		assertEquals(longUrl, actualResponse.getOriginalUrl());
//		assertNotNull(actualResponse.getShortUrlCode()); // Check that shortUrlCode is not null
//		assertEquals(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS), actualResponse.getExpiryDate().truncatedTo(ChronoUnit.SECONDS));
//	}
//
//	@Test
//	@DisplayName("test existing url with non expired expiry date")
//	void testUnexpiredExistingUrl(){
//		Url existingUrl = new Url();
//		existingUrl.setOriginalUrl(longUrl);
//		existingUrl.setShortUrl(shortUrl);
//		existingUrl.setExpiryDate(dateTime.plusDays(1).truncatedTo(ChronoUnit.SECONDS));
//		existingUrl.setCreatedAt(dateTime.truncatedTo(ChronoUnit.SECONDS));
//
//		when(repo.findByShortUrl(shortUrl)).thenReturn(Optional.of(existingUrl));
//
//		UrlResponseDto responseDto=service.getUrl(shortUrl);
//		assertAll(()->assertEquals(existingUrl.getShortUrl(),responseDto.getShortUrlCode()),
//				()->assertEquals(existingUrl.getOriginalUrl(),responseDto.getOriginalUrl()),
//				()->assertEquals(existingUrl.getExpiryDate(),responseDto.getExpiryDate()));
//	}
//	@Test
//	@DisplayName("test when url exist but expired")
//	void testHasExpiredExistingUrl(){
//		Url existingUrl = new Url();
//		existingUrl.setOriginalUrl(longUrl);
//		existingUrl.setShortUrl(shortUrl);
//		existingUrl.setExpiryDate(dateTime.minusDays(1).truncatedTo(ChronoUnit.SECONDS));
//		existingUrl.setCreatedAt(dateTime.truncatedTo(ChronoUnit.SECONDS));
//
//		when(repo.findByShortUrl(shortUrl)).thenReturn(Optional.of(existingUrl));
//
//		assertThrows(ExpiredUrlException.class, ()-> service.getUrl(shortUrl));
//	}
//	@Test
//	void testShortUrlDoesNotExist(){
//		Url existingUrl = new Url();
//		existingUrl.setOriginalUrl(longUrl);
//		existingUrl.setShortUrl(shortUrl);
//		existingUrl.setExpiryDate(dateTime.minusDays(1).truncatedTo(ChronoUnit.SECONDS));
//		existingUrl.setCreatedAt(dateTime.truncatedTo(ChronoUnit.SECONDS));
//		when(repo.findByShortUrl(shortUrl)).thenReturn(Optional.empty());
//		assertThrows(ResourceNotFoundException.class, ()-> service.getUrl(shortUrl));
//	}
//
//	@Test
//	void updateUrl() {
//		Url existingUrl = new Url();
//		existingUrl.setOriginalUrl("http://example.com");
//		existingUrl.setShortUrl(shortUrl);
//		existingUrl.setExpiryDate(LocalDateTime.now().plusDays(1));
//		existingUrl.setCreatedAt(LocalDateTime.now());
//		UrlRequestDto updateDto = new UrlRequestDto();
//		updateDto.setUrl("http://updated-example.com");
//		updateDto.setExpiryDate(LocalDateTime.now().plusDays(2));
//		when(repo.findByShortUrl(shortUrl)).thenReturn(Optional.of(existingUrl));
//		when(repo.save(any())).thenReturn(existingUrl);
//		boolean updated = service.updateUrl(updateDto, shortUrl);
//		assertTrue(updated);
//		assertEquals(updateDto.getUrl(), existingUrl.getOriginalUrl());
//		assertEquals(updateDto.getExpiryDate(), existingUrl.getExpiryDate());
//		verify(repo, times(1)).findByShortUrl(shortUrl);
//		verify(repo, times(1)).save(any());
//	}
//	@Test
//	void deleteUrl() {
//		Url existingUrl = new Url();
//		existingUrl.setOriginalUrl("http://example.com");
//		existingUrl.setShortUrl(shortUrl);
//		existingUrl.setExpiryDate(LocalDateTime.now().plusDays(1));
//		existingUrl.setCreatedAt(LocalDateTime.now());
//		when(repo.findByShortUrl(shortUrl)).thenReturn(Optional.of(existingUrl));
//		boolean deleted = service.deleteUrl(shortUrl);
//		assertTrue(deleted);
//	}
//
//}
