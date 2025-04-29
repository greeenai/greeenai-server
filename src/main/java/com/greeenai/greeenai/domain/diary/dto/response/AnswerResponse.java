package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Answer;

public record AnswerResponse(
		Long id,
		String content
) {
	public static AnswerResponse from(Answer answer) {
		return new AnswerResponse(answer.getId(), answer.getContent());
	}
}
