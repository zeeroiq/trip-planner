package com.shri.spring.ai.tripplanner.dto.wikidata;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents a data value, which is polymorphic. The structure of the 'value'
 * field changes based on the 'type' field (e.g., "string", "globecoordinate").
 * <p>
 * Using {@link JsonNode} for the 'value' field provides the most flexibility,
 * allowing the consumer to interpret it based on the 'type' field without
 * needing complex custom deserializers.
 */
public record DataValue(
        @JsonProperty("value") JsonNode value,
        @JsonProperty("type") String type
) {}