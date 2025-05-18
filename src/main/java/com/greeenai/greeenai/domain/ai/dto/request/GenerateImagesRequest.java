package com.greeenai.greeenai.domain.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GenerateImagesRequest(
        @JsonProperty("image_urls") List<String> imageUrls, @JsonProperty("captions") List<String> captions) {
    public static GenerateImagesRequest of(List<String> imageUrls, List<String> captions) {
        return new GenerateImagesRequest(imageUrls, captions);
    }
}
