package com.greeenai.greeenai.domain.diary.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionAnswerRequest(@NotNull Long questionId, @NotBlank Long optionId) {}
