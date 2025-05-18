package com.greeenai.greeenai.domain.ai.dto.request;

import com.greeenai.greeenai.domain.diary.domain.Question;

public record DiaryEntry(String title, String caption, String question, String answer) {
	public static DiaryEntry of(Question question, String answer) {
		return new DiaryEntry(question.getTitle(), question.getCaption(), question.getPrompt(), answer);
	}
}
