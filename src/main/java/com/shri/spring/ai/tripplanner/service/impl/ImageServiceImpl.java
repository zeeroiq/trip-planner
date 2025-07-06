package com.shri.spring.ai.tripplanner.service.impl;

import com.shri.spring.ai.tripplanner.dto.wikidata.ImageResource;
import com.shri.spring.ai.tripplanner.dto.wikidata.WikidataApiResponse;
import com.shri.spring.ai.tripplanner.errors.NoDataFoundException;
import com.shri.spring.ai.tripplanner.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final RestClient wikidataRestClient;
    @Value("${api.wikimedia.url}")
    private String wikimediaUrl;


    @Override
    public WikidataApiResponse getImageClaims(String overpassWikidata) {
        String uriString = UriComponentsBuilder.fromUriString("?action=wbgetclaims&property=P18&entity={overpassWikidata}&format=json")
                .buildAndExpand(overpassWikidata)
                .toUriString();
        return wikidataRestClient.get()
                .uri(uriString)
                .retrieve()
                .body(WikidataApiResponse.class);
    }

    @Retryable(retryFor = {RestClientResponseException.class, NoDataFoundException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Override
    public ImageResource getImage(String imageName) {

        try {

            RestClient commonsClient = wikidataRestClient
                    .mutate()
                    .requestFactory(new SimpleClientHttpRequestFactory()) // since there would be 302 from wikimedia
                    .baseUrl(wikimediaUrl)
                    .build();

            ResponseEntity<byte[]> responseEntity = commonsClient.get()
                    .uri("/{imageName}", imageName)
                    .accept(MediaType.ALL)
                    .retrieve()
                    .toEntity(byte[].class);

            byte[] imageData = responseEntity.getBody();
            MediaType contentType = responseEntity.getHeaders().getContentType();

            if (imageData == null) {
                log.warn("Received successful response for image '{}' but the body was empty.", imageName);
                return null;
            }

            // If the content type isn't specified, default to a generic binary stream type.
            if (contentType == null) {
                log.warn("Content-Type header was missing for image: {}. Defaulting to application/octet-stream.", imageName);
                contentType = MediaType.APPLICATION_OCTET_STREAM;
            }

            // Construct the ImageResource with the raw data and its media type
            return new ImageResource(imageData, contentType);

        } catch (RestClientResponseException e) {
            // This gracefully handles HTTP errors like 404 Not Found
            log.error("Error fetching image '{}'. Status: {}, Body: {}",
                    imageName, e.getStatusCode(), e.getResponseBodyAsString());
            throw new NoDataFoundException();
        }
    }

    @Recover
    public ImageResource recover(Exception e, String imageName) {
        log.info("Recovering from exception for image - {}", imageName);
        return new ImageResource(new byte[0], MediaType.APPLICATION_OCTET_STREAM);
    }
}
