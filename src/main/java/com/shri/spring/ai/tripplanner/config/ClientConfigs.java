package com.shri.spring.ai.tripplanner.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;

/**
 * @seeme - https://github.com/making/retryable-client-http-request-interceptor
 */
@Slf4j
@RequiredArgsConstructor
@EnableRetry
@Configuration
public class ClientConfigs {
    private final OverpassQueries overpassQueries;

    @Value("${api.openmeteo.url}")
    private String openMeteoBaseUrl;
    @Value("${api.wikidata.url}")
    private String wikidataBaseUrl;


    @Bean
    RestClient overpassRestClient() {
        return RestClient.builder()
                .baseUrl(overpassQueries.url())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    @Bean
    RestClient openMeteoRestClient() {
        return RestClient.builder()
                .baseUrl(openMeteoBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    RestClient wikidataRestClient() {
        return RestClient.builder()
                .baseUrl(wikidataBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
