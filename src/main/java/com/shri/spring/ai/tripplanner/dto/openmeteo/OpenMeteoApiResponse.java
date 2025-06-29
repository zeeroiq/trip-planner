package com.shri.spring.ai.tripplanner.dto.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;


public record OpenMeteoApiResponse(
        @JsonProperty("latitude")
        double latitude,

        @JsonProperty("longitude")
        double longitude,

        @JsonProperty("generationtime_ms")
        double generationTimeMs,

        @JsonProperty("utc_offset_seconds")
        int utcOffsetSeconds,

        @JsonProperty("timezone")
        String timezone,

        @JsonProperty("timezone_abbreviation")
        String timezoneAbbreviation,

        @JsonProperty("elevation")
        double elevation,

        @JsonProperty("daily_units")
        DailyUnits dailyUnits,

        @JsonProperty("daily")
        Daily daily
) {
}