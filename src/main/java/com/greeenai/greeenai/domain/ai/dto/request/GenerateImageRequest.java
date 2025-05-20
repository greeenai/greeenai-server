package com.greeenai.greeenai.domain.ai.dto.request;

import java.util.List;

public record GenerateImageRequest(List<DiaryEntry> entries) {
    public static GenerateImageRequest of(List<DiaryEntry> entries) {
        return new GenerateImageRequest(entries);
    }
}
