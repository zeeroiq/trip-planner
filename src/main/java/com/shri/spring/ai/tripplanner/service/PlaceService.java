package com.shri.spring.ai.tripplanner.service;

import com.shri.spring.ai.tripplanner.dto.OverpassApiResponse;

public interface PlaceService {
    OverpassApiResponse getPlaces(String country, String state, String city, String noOfPlaces);
}
