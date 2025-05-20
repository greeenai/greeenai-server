package com.greeenai.greeenai.domain.ai.dto.request;

import java.util.List;

public record GenerateDiaryRequest(List<DiaryEntry> entries) {
    public static GenerateDiaryRequest of(List<DiaryEntry> entries) {
        return new GenerateDiaryRequest(entries);
    }
}
