package com.arjunagi.urlshortner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve CSS files with proper MIME type and cache headers
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .resourceChain(true);
        
        // Serve JS files with proper MIME type and cache headers
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .resourceChain(true);
        
        // Serve image files
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic())
                .resourceChain(true);
        
        // Serve all static resources with fallback
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                .resourceChain(true);
        
        // Explicitly serve robots.txt and sitemap.xml with proper MIME types
        registry.addResourceHandler("/robots.txt")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
                .resourceChain(true);
        
        registry.addResourceHandler("/sitemap.xml")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
                .resourceChain(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}