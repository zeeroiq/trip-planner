package com.shri.spring.ai.tripplanner.controller;

import com.shri.spring.ai.tripplanner.dto.overpass.OverpassApiResponse;
import com.shri.spring.ai.tripplanner.service.PlaceService;
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
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<OverpassApiResponse> getPlaces(@RequestParam(value = "country", required = false, defaultValue = "${default.country}") String country,
                                                         @RequestParam(value = "state", required = false, defaultValue = "${default.state}") String state,
                                                         @RequestParam(value = "city", required = false, defaultValue = "${default.city}") String city,
                                                         @RequestParam(value = "noOfPlaces", required = false, defaultValue = "${default.noOfPlaces}") String noOfPlaces) {

        log.debug("Retrieving place for country: {}, state: {}, city: {}", country, state, city);
        OverpassApiResponse places = placeService.getPlaces(country, state, city, noOfPlaces);
        return ResponseEntity.ok(places);
    }

}
