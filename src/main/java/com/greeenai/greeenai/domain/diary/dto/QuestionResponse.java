package com.greeenai.greeenai.domain.diary.dto;

import com.greeenai.greeenai.domain.diary.domain.Answer;
import com.greeenai.greeenai.domain.diary.domain.Question;

public record QuestionResponse(
		Long id,
		String content
) {
	public static QuestionResponse from(Question question) {
		return new QuestionResponse(question.getId(), question.getContent());
	}
}
