package com.greeenai.greeenai.domain.diary.dto;

import com.greeenai.greeenai.domain.diary.domain.Question;

public record QuestionWithAnswerResponse(
		Long id,
		String content,
		AnswerResponse answer
) {
	public static QuestionWithAnswerResponse from(Question question) {
		return new QuestionWithAnswerResponse(
				question.getId(),
				question.getContent(),
				getQuestionAnswer(question)
		);
	}

	private static AnswerResponse getQuestionAnswer(Question question) {
		return AnswerResponse.from(question.getAnswer());
	}
}
