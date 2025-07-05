package com.shri.spring.ai.tripplanner.dto.overpass;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

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
) {

    /**
     * Represents a single geographical element (a place of interest).
     *
     * @param type The type of the element (e.g., "node").
     * @param id The unique identifier for the element.
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     * @param tags A map of key-value tags describing the element's properties.
     */
    public record PlaceElement(
            String type,
            long id,
            double lat,
            double lon,
            Map<String, String> tags
    ) {}

    /**
     * Contains metadata from the Overpass API response.
     *
     * @param timestampOsmBase The base timestamp for the OSM data.
     * @param timestampAreasBase The base timestamp for the area data.
     * @param copyright The copyright notice for the data.
     */
    public record Osm3s(
            @JsonProperty("timestamp_osm_base") String timestampOsmBase,
            @JsonProperty("timestamp_areas_base") String timestampAreasBase,
            String copyright
    ) {}
}
