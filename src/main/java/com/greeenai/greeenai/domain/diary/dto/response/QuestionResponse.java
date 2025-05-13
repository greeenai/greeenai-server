package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Question;
import java.util.List;

public record QuestionResponse(Long id, String title, String caption, String prompt, List<OptionResponse> options) {
    public static QuestionResponse from(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getCaption(),
                question.getPrompt(),
                getOptions(question));
    }

    private static List<OptionResponse> getOptions(Question question) {
        return question.getOptions().stream().map(OptionResponse::from).toList();
    }
}
