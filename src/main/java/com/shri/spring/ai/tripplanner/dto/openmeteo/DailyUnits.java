package com.shri.spring.ai.tripplanner.dto.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailyUnits(
        @JsonProperty("time")
        String time,

        @JsonProperty("weathercode")
        String weathercode,

        @JsonProperty("temperature_2m_max")
        String temperature2mMax,

        @JsonProperty("temperature_2m_min")
        String temperature2mMin
) {}