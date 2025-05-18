package com.greeenai.greeenai.domain.diary.dto.request;

import java.util.List;

public record AIGenerateQuestionsRequest(List<String> image_urls) {
    public static AIGenerateQuestionsRequest from(List<String> imageUrls) {
        return new AIGenerateQuestionsRequest(imageUrls);
    }
}
