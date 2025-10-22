package com.arjunagi.urlshortner.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name="Short Url request",
        description = "This dto holds the data required to generate or update short url code"
)
public class UrlRequestDto {
    @Schema(
            description = "The long URL for which you want to generate short url",
            example = "https://github.com/arjunagi-a-rehman/url-shortner/tree/main/src/main/java/com/arjunagi/urlshortner"
    )
    @NotEmpty(message = "URL cannot be empty")
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "Invalid URL format")
    private String url;
    @Schema(
            description = "Expiry date you want to assign. note this is optional feild you can remove this from request in this case the expiry date will be 24hrs from creation",
            example = "2024-01-07T13:59:53"
    )
    private LocalDateTime expiryDate;
}
