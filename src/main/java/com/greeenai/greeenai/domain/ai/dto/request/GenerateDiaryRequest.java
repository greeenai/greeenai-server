package com.greeenai.greeenai.domain.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GenerateDiaryRequest(@JsonProperty("entries") List<DiaryEntry> entries) {
    public static GenerateDiaryRequest of(List<DiaryEntry> entries) {
        return new GenerateDiaryRequest(entries);
    }
}
