package com.greeenai.greeenai.domain.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GenerateImageRequest(@JsonProperty("entries") List<DiaryEntry> entries) {
    public static GenerateImageRequest of(List<DiaryEntry> entries) {
        return new GenerateImageRequest(entries);
    }
}
