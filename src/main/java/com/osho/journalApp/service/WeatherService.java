package com.osho.journalApp.service;

import com.osho.journalApp.constants.Placeholders;
import com.osho.journalApp.enums.CacheKeys;
import com.osho.journalApp.cache.AppCache;
import com.osho.journalApp.response.WeatherResponse;
import com.osho.journalApp.util.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final URLValidator urlValidator;
    private final AppCache appCache;


    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    public WeatherService(RestTemplate restTemplate, URLValidator urlValidator, AppCache appCache) {
        this.restTemplate = restTemplate;
        this.urlValidator = urlValidator;
        this.appCache = appCache;
    }

    public WeatherResponse getWeather(String city) {
        if (!urlValidator.isValidCity(city)) {
            throw new IllegalArgumentException("Invalid city name provided");
        }
        String weatherUrl =  appCache.APP_CACHE.get(CacheKeys.WEATHER_API.toString()).replace(Placeholders.API_KEY, apiKey).replace(Placeholders.CITY, city);

        if (!urlValidator.isValidWeatherApiUrl(weatherUrl)) {
            throw new SecurityException("Invalid weather API URL");
        }
        ResponseEntity<WeatherResponse> weatherResponse =  restTemplate.exchange(weatherUrl, HttpMethod.GET, null, WeatherResponse.class);
        if (weatherResponse.getStatusCode().is2xxSuccessful()) {
            return weatherResponse.getBody();
        } else {
            throw new ResponseStatusException(weatherResponse.getStatusCode(), "Failed to get weather data");
        }
    }
}