package com.greeenai.greeenai.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AIGenerateDiaryResponse(@JsonProperty("diary") String diary) {}
