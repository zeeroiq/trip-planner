package com.shri.spring.ai.tripplanner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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