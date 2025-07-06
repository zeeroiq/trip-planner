package com.shri.spring.ai.tripplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shri.spring.ai.tripplanner.dto.openmeteo.OpenMeteoApiResponse;
import com.shri.spring.ai.tripplanner.dto.overpass.OverpassApiResponse;
import com.shri.spring.ai.tripplanner.dto.wikidata.ImageResource;
import lombok.Builder;

@Builder
public record PlaceRecommendation(@JsonProperty("place") OverpassApiResponse.PlaceElement place,
                                  @JsonProperty("imageResource") ImageResource imageResource,
                                  @JsonProperty("imageUrl") String imageUrl,
                                  @JsonProperty("weatherDetails") OpenMeteoApiResponse weatherDetails) {
}
