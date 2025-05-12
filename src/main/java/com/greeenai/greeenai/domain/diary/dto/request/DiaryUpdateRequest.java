package com.greeenai.greeenai.domain.diary.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DiaryUpdateRequest(@NotNull LocalDate entryDate) {}
