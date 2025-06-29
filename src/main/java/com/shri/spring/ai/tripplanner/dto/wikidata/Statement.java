package com.shri.spring.ai.tripplanner.dto.wikidata;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Statement(
        @JsonProperty("mainsnak") Snak mainsnak,
        @JsonProperty("type") String type,
        @JsonProperty("id") String id,
        @JsonProperty("rank") String rank,
        @JsonProperty("references") List<Reference> references
) {}