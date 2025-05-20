package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Question;
import java.util.List;

public record QuestionResponse(Long id, String prompt, List<OptionResponse> options) {
    public static QuestionResponse from(Question question) {
        return new QuestionResponse(question.getId(), question.getPrompt(), getOptions(question));
    }

    private static List<OptionResponse> getOptions(Question question) {
        return question.getOptions().stream().map(OptionResponse::from).toList();
    }
}
