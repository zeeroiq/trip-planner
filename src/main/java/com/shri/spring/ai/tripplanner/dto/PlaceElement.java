package com.shri.spring.ai.tripplanner.dto;

import java.util.Map;

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