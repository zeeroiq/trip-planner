package com.shri.spring.ai.tripplanner.dto.wikidata;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record Reference(
        @JsonProperty("hash") String hash,
        @JsonProperty("snaks") Map<String, List<Snak>> snaks,
        @JsonProperty("snaks-order") List<String> snaksOrder
) {}