package com.shri.spring.ai.tripplanner.dto.openmeteo;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Daily(
        @JsonProperty("time")
        List<String> time,

        @JsonProperty("weathercode")
        List<Integer> weathercode,

        @JsonProperty("temperature_2m_max")
        List<Double> temperature2mMax,

        @JsonProperty("temperature_2m_min")
        List<Double> temperature2mMin
) {}