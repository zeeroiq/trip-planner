package com.shri.spring.ai.tripplanner.service;

import com.shri.spring.ai.tripplanner.dto.openmeteo.OpenMeteoApiResponse;

public interface WeatherService {

    OpenMeteoApiResponse getWeather(Double lat, Double lon);
}
