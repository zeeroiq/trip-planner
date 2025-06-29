package com.shri.spring.ai.tripplanner.dto.wikidata;


import com.fasterxml.jackson.annotation.JsonProperty;

public record Snak(
        @JsonProperty("snaktype") String snaktype,
        @JsonProperty("property") String property,
        @JsonProperty("hash") String hash,
        @JsonProperty("datavalue") DataValue datavalue,
        @JsonProperty("datatype") String datatype
) {}