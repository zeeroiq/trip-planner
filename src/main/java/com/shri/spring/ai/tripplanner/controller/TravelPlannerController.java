package com.shri.spring.ai.tripplanner.controller;

import com.shri.spring.ai.tripplanner.dto.PlaceRecommendation;
import com.shri.spring.ai.tripplanner.service.TripPlannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/travel-planner")
public class TravelPlannerController {

    private final TripPlannerService tripPlannerService;

    @GetMapping
    public ResponseEntity<List<PlaceRecommendation>> getPlaceRecommendation(@RequestParam(value = "country", required = false, defaultValue = "${default.country}") String country,
                                                                            @RequestParam(value = "state", required = false, defaultValue = "${default.state}") String state,
                                                                            @RequestParam(value = "city", required = false, defaultValue = "${default.city}") String city,
                                                                            @RequestParam(value = "noOfPlaces", required = false, defaultValue = "${default.noOfPlaces}") int noOfPlaces,
                                                                            @RequestParam(value = "travelDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate) {

        log.debug("Retrieving place recommendation for country: {}, state: {}, city: {}, noOfPlaces: {}, travelDate: {}", country, state, city, noOfPlaces, travelDate);
        List<PlaceRecommendation> placeRecommendations = tripPlannerService.getPlaceRecommendation(country, state, city, noOfPlaces, travelDate != null ? travelDate.toString() : null);
        return ResponseEntity.ok(placeRecommendations);
    }

}
