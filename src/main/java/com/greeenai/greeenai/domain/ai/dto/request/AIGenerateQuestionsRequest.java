package com.greeenai.greeenai.domain.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AIGenerateQuestionsRequest(@JsonProperty("image_urls") List<String> imageUrls) {
    public static AIGenerateQuestionsRequest from(List<String> imageUrls) {
        return new AIGenerateQuestionsRequest(imageUrls);
    }
}
