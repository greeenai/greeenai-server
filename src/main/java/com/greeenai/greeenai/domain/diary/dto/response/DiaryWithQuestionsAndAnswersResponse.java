package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Diary;

import java.time.LocalDate;
import java.util.List;

public record DiaryWithQuestionsAndAnswersResponse(
		Long id,
		String content,
		LocalDate entryDate,
		List<QuestionWithAnswerResponse> questions
) {
	public static DiaryWithQuestionsAndAnswersResponse from(Diary diary) {
		return new DiaryWithQuestionsAndAnswersResponse(
				diary.getId(),
				diary.getContent(),
				diary.getEntryDate(),
				getDiaryQuestionsAndAnswers(diary)
		);
	}

	private static List<QuestionWithAnswerResponse> getDiaryQuestionsAndAnswers(Diary diary) {
		return diary.getQuestions().stream()
				.map(QuestionWithAnswerResponse::from)
				.toList();
	}
}
