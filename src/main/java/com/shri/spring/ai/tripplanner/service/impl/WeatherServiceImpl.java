package com.shri.spring.ai.tripplanner.service.impl;

import com.shri.spring.ai.tripplanner.dto.openmeteo.OpenMeteoApiResponse;
import com.shri.spring.ai.tripplanner.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final RestClient openMeteoRestClient;

    @Override
    public OpenMeteoApiResponse getWeather(Double lat, Double lon) {
        log.debug("Retrieving weather for lat: {}, lon: {}", lat, lon);

        String uri = UriComponentsBuilder.fromUriString("/forecast?latitude={latitude}&longitude={longitude}&daily=weathercode,temperature_2m_max,temperature_2m_min&timezone=auto")
                .buildAndExpand(lat, lon)
                .toUriString();
        return openMeteoRestClient.get()
                .uri(uri)
                .retrieve()
                .body(OpenMeteoApiResponse.class);
    }
}
