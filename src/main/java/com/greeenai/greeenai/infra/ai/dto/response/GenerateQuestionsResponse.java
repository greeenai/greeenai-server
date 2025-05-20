package com.greeenai.greeenai.infra.ai.dto.response;

import java.util.List;

public record GenerateQuestionsResponse(List<GeneratedQuestion> questions) {

    public record GeneratedQuestion(String title, String caption, String prompt, List<String> options) {}
}
