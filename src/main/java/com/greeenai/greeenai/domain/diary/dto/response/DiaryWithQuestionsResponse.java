package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import java.time.LocalDate;
import java.util.List;

public record DiaryWithQuestionsResponse(
        Long id, String imageUrl, String content, LocalDate entryDate, List<QuestionResponse> questions) {
    public static DiaryWithQuestionsResponse from(Diary diary, String imageUrl) {
        return new DiaryWithQuestionsResponse(
                diary.getId(), imageUrl, diary.getContent(), diary.getEntryDate(), getDiaryQuestions(diary));
    }

    private static List<QuestionResponse> getDiaryQuestions(Diary diary) {
        return diary.getQuestions().stream().map(QuestionResponse::from).toList();
    }
}
