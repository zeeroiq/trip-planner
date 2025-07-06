package com.shri.spring.ai.tripplanner.service.impl;

import com.shri.spring.ai.tripplanner.dto.PlaceRecommendation;
import com.shri.spring.ai.tripplanner.dto.openmeteo.Daily;
import com.shri.spring.ai.tripplanner.dto.openmeteo.OpenMeteoApiResponse;
import com.shri.spring.ai.tripplanner.dto.overpass.OverpassApiResponse;
import com.shri.spring.ai.tripplanner.dto.wikidata.ImageResource;
import com.shri.spring.ai.tripplanner.dto.wikidata.Statement;
import com.shri.spring.ai.tripplanner.dto.wikidata.WikidataApiResponse;
import com.shri.spring.ai.tripplanner.service.ImageService;
import com.shri.spring.ai.tripplanner.service.PlaceService;
import com.shri.spring.ai.tripplanner.service.TripPlannerService;
import com.shri.spring.ai.tripplanner.service.WeatherService;
import io.micrometer.common.util.StringUtils;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
@Service
public class TripPlannerServiceImpl implements TripPlannerService {

    private final PlaceService placeService;
    private final ImageService imageService;
    private final WeatherService weatherService;

    @Value("${api.wikimedia.url}")
    private String wikimediaUrl;

    @Observed(name = "trip-planner-service")
    @Override
    public List<PlaceRecommendation> getPlaceRecommendation(String country, String state, String city, int noOfPlaces, String travelDate) {

        OverpassApiResponse places = placeService.getPlaces(country, state, city, String.valueOf(noOfPlaces));
        log.info("called place recommendation service");
        return places.elements().stream()
                .parallel()
                .map(place -> {

                    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) { // java 24 specific
                        long placeId = place.id();
                        double lat = place.lat();
                        double lon = place.lon();
                        String placeName = place.tags().getOrDefault("placeName", "NA");
                        String wikidataId = place.tags().get("wikidata");
                        if (StringUtils.isNotEmpty(wikidataId)) {
                            log.error("Wikidata not available in overpass response for {} ", placeName);
                        }

                        var weatherSubTask = scope.fork(() -> weatherService.getWeather(lat, lon));
                        var imageClaimsSubsTask = scope.fork(() -> imageService.getImageClaims(wikidataId));
                        log.info("Current thread : {}", Thread.currentThread());

                        scope.join();
                        scope.throwIfFailed();

                        OpenMeteoApiResponse weather = weatherSubTask.get();
                        OpenMeteoApiResponse finalWeather = weather;
                        if (StringUtils.isNotEmpty(travelDate) && weather != null && weather.daily() != null) {
                            List<String> dailyTimes = weather.daily().time();
                            int travelDateIndex = dailyTimes.indexOf(travelDate);

                            if (travelDateIndex != -1) {
                                var daily = weather.daily();
                                var singleDayData = new Daily(
                                        List.of(daily.time().get(travelDateIndex)),
                                        List.of(daily.weathercode().get(travelDateIndex)),
                                        List.of(daily.temperature2mMax().get(travelDateIndex)),
                                        List.of(daily.temperature2mMin().get(travelDateIndex))
                                );

                                finalWeather = new OpenMeteoApiResponse(
                                        weather.latitude(),
                                        weather.longitude(),
                                        weather.generationTimeMs(),
                                        weather.utcOffsetSeconds(),
                                        weather.timezone(),
                                        weather.timezoneAbbreviation(),
                                        weather.elevation(),
                                        weather.dailyUnits(),
                                        singleDayData
                                );
                            } else {
                                log.warn("For property {} travel date {} not found in weather forecast for {}. Full forecast will be used.", placeId, travelDate, placeName);
                            }
                        }

                        WikidataApiResponse imageClaims = imageClaimsSubsTask.get();

                        AtomicReference<ImageResource> imageResource = new AtomicReference<>();
                        AtomicReference<String> imageUrl = new AtomicReference<>();
                        if (Objects.nonNull(imageClaims) && !CollectionUtils.isEmpty(imageClaims.claims())) {
                            List<Statement> p18 = imageClaims.claims().get("P18");
                            p18.stream()
                                    .filter(item -> item.rank().equals("preferred"))
                                    .findFirst()
                                    .map(item -> item.mainsnak().datavalue().value())
                                    .ifPresent(imageNode -> {
                                        imageResource.set(imageService.getImage(String.valueOf(imageNode.textValue())));
                                        imageUrl.set(wikimediaUrl.concat(String.valueOf(imageNode.textValue())));
                                    });
                        }
                        return PlaceRecommendation.builder()
                                .place(place)
                                .weatherDetails(finalWeather)
                                .imageResource(imageResource.get())
                                .imageUrl(imageUrl.get())
                                .build();
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
