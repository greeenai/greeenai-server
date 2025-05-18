package com.greeenai.greeenai.domain.ai.dto.response;

import java.util.List;

public record AIQuestionResponse(String title, String caption, String prompt, List<String> options) {}
