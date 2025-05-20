package com.greeenai.greeenai.infra.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GenerateQuestionsRequest(@JsonProperty("image_urls") List<String> imageUrls) {
    public static GenerateQuestionsRequest from(List<String> imageUrls) {
        return new GenerateQuestionsRequest(imageUrls);
    }
}
