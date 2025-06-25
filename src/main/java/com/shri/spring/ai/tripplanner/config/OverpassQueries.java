package com.shri.spring.ai.tripplanner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "overpass")
public record OverpassQueries(
        String url,
        Country country,
        State state,
        City city) {

    public record Country(String body, State state, City city){}
    public record State(String body, City city){}
    public record City(String body){}
}
