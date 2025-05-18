package com.greeenai.greeenai.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateDiaryResponse(@JsonProperty("diary") String diary) {}
