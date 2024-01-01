package com.arjunagi.urlshortner.controller;

import com.arjunagi.urlshortner.dtos.ResponseDto;
import com.arjunagi.urlshortner.dtos.UrlRequestDto;
import com.arjunagi.urlshortner.dtos.UrlResponseDto;
import com.arjunagi.urlshortner.services.IUrlServices;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@Validated
public class UrlController {
    IUrlServices urlServices;
    HttpServletResponse response;

    @PostMapping("/url")
    public ResponseEntity<UrlResponseDto> generateShortUrl(@RequestBody @Valid UrlRequestDto urlRequestDto){
        return new ResponseEntity<>(urlServices.generateShortUrl(urlRequestDto), HttpStatus.CREATED);
    }
    @GetMapping("/{shortUrl}")
    public ResponseEntity<ResponseDto> getOriginal(@PathVariable @NotEmpty String shortUrl) throws IOException {
        response.sendRedirect(urlServices.getUrl(shortUrl));
        return null;
    }
    @PutMapping("/url")
    public ResponseEntity<ResponseDto> updateUrl(@RequestBody @Valid UrlRequestDto urlRequestDto){
        boolean updated=urlServices.updateUrl(urlRequestDto);
        return ResponseEntity.ok(new ResponseDto("200","updated successfully"));
    }

    @DeleteMapping("/url/{shortUrl}")
    public ResponseEntity<ResponseDto> deleteUrl(@PathVariable @NotEmpty String shortUrl){
        boolean isDeleted=urlServices.deleteUrl(shortUrl);
        return ResponseEntity.ok(new ResponseDto("200","deleted successfully"));
    }
}
