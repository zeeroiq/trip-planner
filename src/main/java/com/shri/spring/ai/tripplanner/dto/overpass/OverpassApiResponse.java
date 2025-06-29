package com.shri.spring.ai.tripplanner.dto.overpass;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the top-level response from the Overpass API.
 *
 * @param version The API version number.
 * @param generator Information about the API generator.
 * @param osm3s Metadata and timestamps.
 * @param elements A list of geographical elements found.
 */
public record OverpassApiResponse(
        double version,
        String generator,
        Osm3s osm3s,
        @JsonProperty("elements") List<PlaceElement> elements
) {}
