package com.shri.spring.ai.tripplanner.service.impl;

import com.shri.spring.ai.tripplanner.dto.OverpassApiResponse;
import com.shri.spring.ai.tripplanner.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final RestClient restClient;

    @Value("${overpass.url}")
    private String overpassUrl;
    @Value("${overpass.country.body}")
    private String overpassCountryBody;
    @Value("${overpass.country.state.body}")
    private String overpassCountryStateBody;
    @Value("${overpass.country.state.city.body}")
    private String overpassCountryStateCityBody;
    @Value("${overpass.country.city.body}")
    private String overpassCountryCityBody;
    @Value("${overpass.state.body}")
    private String overpassStateBody;
    @Value("${overpass.state.city.body}")
    private String overpassStateCityBody;
    @Value("${overpass.city.body}")
    private String overpassCityBody;


    @Override
    public OverpassApiResponse getPlaces(String country, String state, String city, String noOfPlaces) {
        String requestBody = "";
        if (StringUtils.isNotEmpty(country) && StringUtils.isNotEmpty(state) && StringUtils.isNotEmpty(city)) {
            requestBody = overpassCountryStateCityBody
                    .replace("{{country}}", country)
                    .replace("{{state}}", state)
                    .replace("{{city}}", city);
        } else if (StringUtils.isNotEmpty(country) && StringUtils.isNotEmpty(state)) {
            requestBody = overpassCountryStateBody
                    .replace("{{country}}", country)
                    .replace("{{state}}", state);
        } else if (StringUtils.isNotEmpty(country) && StringUtils.isNotEmpty(city)) {
            requestBody = overpassCountryCityBody
                    .replace("{{country}}", country)
                    .replace("{{city}}", city);
        } else if (StringUtils.isNotEmpty(state) && StringUtils.isNotEmpty(city)) {
            requestBody = overpassStateCityBody
                    .replace("{{state}}", state)
                    .replace("{{city}}", city);
        } else if (StringUtils.isNotEmpty(country)) {
            requestBody = overpassCountryBody
                    .replace("{{country}}", country);
        } else if (StringUtils.isNotEmpty(state)) {
            requestBody = overpassStateBody
                    .replace("{{state}}", state);
        } else if (StringUtils.isNotEmpty(city)) {
            requestBody = overpassCityBody
                    .replace("{{city}}", city);
        }

        if (StringUtils.isEmpty(requestBody)) {
            log.error("No location parameters provided, cannot build Overpass query.");
            return null;
        }
        requestBody = requestBody.replace("{{noOfPlaces}}", noOfPlaces);
        log.debug("Executing Overpass query: {}}", requestBody);

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
                .uri(overpassUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(OverpassApiResponse.class);

    }
}

