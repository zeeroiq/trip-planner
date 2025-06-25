package com.shri.spring.ai.tripplanner.service.impl;

import com.shri.spring.ai.tripplanner.config.OverpassQueries;
import com.shri.spring.ai.tripplanner.dto.OverpassApiResponse;
import com.shri.spring.ai.tripplanner.service.PlaceService;
import com.shri.spring.ai.tripplanner.util.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(OverpassQueries.class)
public class PlaceServiceImpl implements PlaceService {

    private final RestClient restClient;
    private final OverpassQueries overpassQueries;

    @Value("${default.noOfPlaces}")
    private String defaultNoOfPlacesToSearch;


    @Override
    public OverpassApiResponse getPlaces(String country, String state, String city, String noOfPlaces) {
        if (!StringUtils.isNumeric(noOfPlaces)) {
            log.error("noOfPlaces must be numeric, falling back to default {}", defaultNoOfPlacesToSearch);
            noOfPlaces = defaultNoOfPlacesToSearch;
        }
        String template = getTemplate(country, state, city);

        HashMap<String, String> valueMap = new HashMap<>();
        valueMap.put("country", country);
        valueMap.put("state", state);
        valueMap.put("city", city);
        valueMap.put("noOfPlaces", noOfPlaces);

        StringSubstitutor sub = new StringSubstitutor(valueMap, "{{", "}}");
        String requestBody = sub.replace(template);
        log.debug("Executing Overpass query: \n{}", requestBody);

        /* *
         * curl --location 'https://overpass-api.de/api/interpreter' \
         * --header 'Content-Type: application/x-www-form-urlencoded' \
         * --data-urlencode 'data=[out:json];area["name"="Australia"]->.country;area["name"="Sydney"](area.country)->.searchArea;(node["tourism"="attraction"](area.searchArea);way["tourism"="attraction"](area.searchArea);relation["tourism"="attraction"](area.searchArea););out center 5;'
         * */
        // The curl command uses --data-urlencode 'data=...' which is a POST request
        // with a form-urlencoded body. The key is 'data'.
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("data", requestBody);

        return restClient.post()
                .uri(overpassQueries.url())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(OverpassApiResponse.class);

    }

    private String getTemplate(String country, String state, String city) {

        // switch expression (Java 14+) with pattern matching (Java 21)
        return switch (new Location(country, state, city)) {
            case Location(var c, var s, var ci) when StringUtils.isNotEmpty(c) && StringUtils.isNotEmpty(s) && StringUtils.isNotEmpty(ci) ->  overpassQueries.country().state().city().body();
            case Location(var c, var s, _) when StringUtils.isNotEmpty(c) && StringUtils.isNotEmpty(s) ->  overpassQueries.country().state().body();
            case Location(var c, _, var ci) when StringUtils.isNotEmpty(c) && StringUtils.isNotEmpty(ci) ->  overpassQueries.country().city().body();
            case Location(_, var s, var ci) when StringUtils.isNotEmpty(s) && StringUtils.isNotEmpty(ci) ->  overpassQueries.state().city().body();
            case Location(var c, _, _) when StringUtils.isNotEmpty(c) ->  overpassQueries.country().body();
            case Location(_, var s, _) when StringUtils.isNotEmpty(s) ->  overpassQueries.state().body();
            case Location(_, _, var ci) when StringUtils.isNotEmpty(ci) ->  overpassQueries.city().body();
            default -> {
                log.error("No valid combination of location parameters provided, cannot build Overpass query.");
                throw new IllegalArgumentException("A valid combination of country, state, or city must be provided.");
            }
        };
    }
}

