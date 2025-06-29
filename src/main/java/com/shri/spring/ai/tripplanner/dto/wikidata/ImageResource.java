package com.shri.spring.ai.tripplanner.dto.wikidata;

import org.springframework.http.MediaType;

/**
 * A record to hold image data and its corresponding media type.
 *
 * @param data The raw byte array of the image.
 * @param mediaType The MediaType of the image (e.g., IMAGE_JPEG, IMAGE_PNG).
 */
public record ImageResource(byte[] data, MediaType mediaType) {}
