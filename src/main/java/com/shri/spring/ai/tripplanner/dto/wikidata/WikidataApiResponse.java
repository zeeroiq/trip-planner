package com.shri.spring.ai.tripplanner.dto.wikidata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record WikidataApiResponse(
        @JsonProperty("claims") Map<String, List<Statement>> claims
) {}
