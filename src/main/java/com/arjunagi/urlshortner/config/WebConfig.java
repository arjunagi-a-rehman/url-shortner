package com.arjunagi.urlshortner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        
        // Explicitly serve robots.txt and sitemap.xml with proper MIME types
        registry.addResourceHandler("/robots.txt")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(86400) // Cache for 1 day
                .resourceChain(true);
        
        registry.addResourceHandler("/sitemap.xml")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(86400) // Cache for 1 day
                .resourceChain(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    // Content negotiation removed to avoid conflicts with actuator endpoints
}