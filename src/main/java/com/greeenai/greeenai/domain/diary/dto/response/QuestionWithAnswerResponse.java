package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Option;
import com.greeenai.greeenai.domain.diary.domain.Question;

public record QuestionWithAnswerResponse(Long id, String prompt, OptionResponse answer) {
    public static QuestionWithAnswerResponse from(Question question) {
        return new QuestionWithAnswerResponse(question.getId(), question.getPrompt(), getQuestionAnswer(question));
    }

    private static OptionResponse getQuestionAnswer(Question question) {
        Option answer = question.getOptions().stream()
                .filter(Option::isAnswer)
                .findFirst()
                .orElse(null);
        return answer == null ? null : OptionResponse.from(answer);
    }
}
