package com.greeenai.greeenai.domain.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AIGenerateImagesRequest(
        @JsonProperty("image_urls") List<String> imageUrls, @JsonProperty("captions") List<String> captions) {
    public static AIGenerateImagesRequest of(List<String> imageUrls, List<String> captions) {
        return new AIGenerateImagesRequest(imageUrls, captions);
    }
}
