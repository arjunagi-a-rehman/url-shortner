package com.arjunagi.urlshortner.controller;

import com.arjunagi.urlshortner.dtos.ErrorResponseDto;
import com.arjunagi.urlshortner.dtos.ResponseDto;
import com.arjunagi.urlshortner.dtos.UrlRequestDto;
import com.arjunagi.urlshortner.dtos.UrlResponseDto;
import com.arjunagi.urlshortner.services.IUrlServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "APIs to create update, and delete short URL",
        description = "The apis for creating short URL from the long url and perform the curl operation"
)
@CrossOrigin(origins = "*")
public class UrlController { // dependencies are getting injected by constructor thanks to Lomboks @ AllArgConstructor
    IUrlServices urlServices;
    HttpServletResponse response; // for redirect operation

    // Please refer to com.arjunagi.rulshortner.exception.GlobalExceptionHandler.java to understand error codes and error handling

//=============================================================================================================================================
    @Operation(
            summary = "Creates the short url from the long url",
            description = "you need to provide long url and expiry date( optional if not provided explicitly the url validity will be 24 hrs" +
                    "note you'll get short url code to access and and utilise the url need to attach after domain name e.g. https://sus9.in/8uil901,"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/url")
    public ResponseEntity<UrlResponseDto> generateShortUrl(@RequestBody @Valid UrlRequestDto urlRequestDto){
        return new ResponseEntity<>(urlServices.generateShortUrl(urlRequestDto), HttpStatus.CREATED);
    }

    //==============================================================================================================================================================================

    @Operation(
            summary = "this will redirect you to original url note if your utilizing this from the swagger Ui it won't redirect it.",
            description = "If your utilizing this api from browser then you need to do https://sus9.in/{shot url code} then it will redirect you to the original long url and if " +
                    "your utilizing this from the swagger you wont get any response it will tell you their is some cors problem because I am redirecting the request to the original url"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "will redirect to the original url"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/{shortUrlCode}")
    public void getOriginal(@PathVariable @NotEmpty String shortUrlCode) throws IOException {
        UrlResponseDto urlResponseDto=urlServices.getUrl(shortUrlCode);
        response.sendRedirect(urlResponseDto.getOriginalUrl());
    }
    //==========================================================================================================================
    @Operation(
            summary = "get the details regarding short url code",
            description = "This api provide all the related details regarding the short url code like, original url, expiry date e.t.c"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status ok",
                    content = @Content(
                            schema = @Schema(implementation = UrlResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )

    @GetMapping("/details/{shortUrlCode}")
    public ResponseEntity<UrlResponseDto> getUrlDetails(@PathVariable String shortUrlCode){
        return ResponseEntity.ok(urlServices.getUrl(shortUrlCode));
    }

    //========================================================================================================================

    @Operation(
            summary = "update the expiry date and the original for a particular short url code",
            description = "The short url code for each request will be unique using this api you can update the original url to which this short your code was pointing to also you can use this to update the expiry timem" +
                    "note:- other than the short url both the parameters of request body is optional if you don't provide anything then expiry  date will be increased by 24 hrs "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PutMapping("/url")
    public ResponseEntity<ResponseDto> updateUrl(@RequestBody(required = false) @Valid UrlRequestDto urlRequestDto,@RequestParam @NotEmpty String shortUrlCode){
        boolean updated=urlServices.updateUrl(urlRequestDto,shortUrlCode);
        return ResponseEntity.ok(new ResponseDto("200","updated successfully"));
    }

    //========================================================================================================================

    @Operation(
            summary = "delete the url record based on the  short url",
            description = "This api is used to delete url record permanently, you can't use the short url for a particular long url once you delete record "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @DeleteMapping("/url/{shortUrl}")
    public ResponseEntity<ResponseDto> deleteUrl(@PathVariable @NotEmpty String shortUrl){
        boolean isDeleted=urlServices.deleteUrl(shortUrl);
        return ResponseEntity.ok(new ResponseDto("200","deleted successfully"));
    }
}
