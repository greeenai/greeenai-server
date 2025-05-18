package com.greeenai.greeenai.domain.diary.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AIGenerateDiaryRequest(@JsonProperty("entries") List<DiaryEntry> entries) {
    public static AIGenerateDiaryRequest of(List<DiaryEntry> entries) {
        return new AIGenerateDiaryRequest(entries);
    }

    public record DiaryEntry(String title, String caption, String question, String answer) {}
}
