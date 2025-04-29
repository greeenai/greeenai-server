package com.greeenai.greeenai.domain.diary.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DiaryCreateRequest(
		@NotNull LocalDate entryDate) {}

// TODO : 그림일기 생성용 사진 입력받기