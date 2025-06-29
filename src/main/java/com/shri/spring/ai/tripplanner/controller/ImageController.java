package com.shri.spring.ai.tripplanner.controller;

import com.shri.spring.ai.tripplanner.dto.wikidata.ImageResource;
import com.shri.spring.ai.tripplanner.dto.wikidata.WikidataApiResponse;
import com.shri.spring.ai.tripplanner.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/claims")
    public ResponseEntity<WikidataApiResponse> getImageClaims(@RequestParam("overpassWikidata") String overpassWikidata) {
        log.debug("Getting image claims for overpassWikidata: {}", overpassWikidata);
        WikidataApiResponse imageClaims = imageService.getImageClaims(overpassWikidata);
        log.debug("Image claims: {}", imageClaims);
        return ResponseEntity.ofNullable(imageClaims);
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        log.debug("Request to get image: {}", imageName);
        ImageResource imageResource = imageService.getImage(imageName);

        if (imageResource == null) {
            log.warn("Image not found: {}", imageName);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(imageResource.mediaType()) // e.g., MediaType.IMAGE_JPEG
                .contentLength(imageResource.data().length)
                .body(imageResource.data());
    }
}
