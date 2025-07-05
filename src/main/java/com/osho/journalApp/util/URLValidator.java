package com.osho.journalApp.util;

import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Component
public class URLValidator {
    private static final List<String> ALLOWED_DOMAINS = Collections.singletonList(
            "api.weatherstack.com"
    );

    public boolean isValidWeatherApiUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost().toLowerCase();
            
            // Check if the host is in our allowed list
            return ALLOWED_DOMAINS.stream()
                .anyMatch(host::equals);
                
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidCity(String city) {
        return city != null 
            && !city.trim().isEmpty() 
            && city.length() <= 100 
            && city.matches("^[a-zA-Z\\s,.-]+$");
    }
}