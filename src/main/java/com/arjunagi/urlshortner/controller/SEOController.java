package com.arjunagi.urlshortner.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SEOController {

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getSitemap() {
        // Sitemap is served as static file, but this endpoint can be used for dynamic generation
        return ResponseEntity.ok()
                .header("Content-Type", "application/xml")
                .body("<!-- Sitemap served as static file -->");
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getRobots() {
        // Robots.txt is served as static file, but this endpoint can be used for dynamic generation
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body("# Robots.txt served as static file");
    }
}