package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import java.time.LocalDate;

public record DiaryResponse(Long id, String content, LocalDate entryDate) {
    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(diary.getId(), diary.getContent(), diary.getEntryDate());
    }
}
