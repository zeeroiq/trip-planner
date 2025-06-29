package com.shri.spring.ai.tripplanner.controller;

import com.shri.spring.ai.tripplanner.dto.openmeteo.OpenMeteoApiResponse;
import com.shri.spring.ai.tripplanner.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<OpenMeteoApiResponse> getWeather(@RequestParam("lat") Double lat, @RequestParam("lon") Double lon) {
        log.debug("Retrieving weather for lat: {}, lon: {}", lat, lon);
        OpenMeteoApiResponse response = weatherService.getWeather(lat, lon);
        log.debug("Response: {}", response);
        return ResponseEntity.ok(response);
    }
}
