package com.shri.spring.ai.tripplanner.service;

import com.shri.spring.ai.tripplanner.dto.wikidata.ImageResource;
import com.shri.spring.ai.tripplanner.dto.wikidata.WikidataApiResponse;

public interface ImageService {
    WikidataApiResponse getImageClaims(String overpassWikidata);
    ImageResource getImage(String imageName);
}
