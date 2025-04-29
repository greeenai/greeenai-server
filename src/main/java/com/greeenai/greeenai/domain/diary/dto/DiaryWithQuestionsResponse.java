package com.greeenai.greeenai.domain.diary.dto;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.diary.domain.Question;

import java.time.LocalDate;
import java.util.List;

public record DiaryWithQuestionsResponse(
		Long id,
		String content,
		LocalDate entryDate,
		List<String> questions
) {
	public static DiaryWithQuestionsResponse from(Diary diary) {
		return new DiaryWithQuestionsResponse(
				diary.getId(),
				diary.getContent(),
				diary.getEntryDate(),
				getDiaryQuestions(diary)
		);
	}

	private static List<String> getDiaryQuestions(Diary diary) {
		return diary.getQuestions().stream()
				.map(Question::getContent)
				.toList();
	}
}
