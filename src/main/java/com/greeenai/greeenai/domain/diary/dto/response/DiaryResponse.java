package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import java.time.LocalDate;

public record DiaryResponse(Long id, String imageUrl, String content, LocalDate entryDate) {
    public static DiaryResponse of(Diary diary, String imageUrl) {
        return new DiaryResponse(diary.getId(), imageUrl, diary.getContent(), diary.getEntryDate());
    }
}
