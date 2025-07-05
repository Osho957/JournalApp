package com.osho.journalApp.controller;

import com.osho.journalApp.response.WeatherResponse;
import com.osho.journalApp.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current/{city}")
    public ResponseEntity<?> getCurrentWeather(@PathVariable String city) {
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        return ResponseEntity.ok(weatherResponse);
    }
}