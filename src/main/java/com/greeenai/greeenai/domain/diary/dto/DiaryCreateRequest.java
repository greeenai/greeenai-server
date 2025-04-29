package com.greeenai.greeenai.domain.diary.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record DiaryCreateRequest(
		@NotBlank LocalDate entryDate
) {}
