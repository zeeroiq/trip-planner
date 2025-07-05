package com.shri.spring.ai.tripplanner.service;

import com.shri.spring.ai.tripplanner.dto.PlaceRecommendation;

import java.util.List;

public interface TripPlannerService {
    List<PlaceRecommendation> getPlaceRecommendation(String country, String state, String city, int noOfPlaces, String travelDate);
}
