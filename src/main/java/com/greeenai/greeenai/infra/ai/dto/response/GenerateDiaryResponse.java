package com.greeenai.greeenai.infra.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateDiaryResponse(@JsonProperty("diary") String diaryContent) {}
