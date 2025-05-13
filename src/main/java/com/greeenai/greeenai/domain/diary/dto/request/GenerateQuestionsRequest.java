package com.greeenai.greeenai.domain.diary.dto.request;

import java.util.List;

public record GenerateQuestionsRequest(List<String> image_urls) {
    public static GenerateQuestionsRequest from(List<String> imageUrls) {
        return new GenerateQuestionsRequest(imageUrls);
    }
}
