package com.arjunagi.urlshortner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class AppConfig {
    
    @Value("${app.domain.local:http://localhost:8080}")
    private String localDomain;
    
    @Value("${app.domain.production:https://sus9.in}")
    private String productionDomain;
    
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    
    public String getBaseDomain() {
        return "prod".equals(activeProfile) ? productionDomain : localDomain;
    }
}