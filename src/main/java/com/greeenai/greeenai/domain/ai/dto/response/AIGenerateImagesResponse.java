package com.greeenai.greeenai.domain.ai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AIGenerateImagesResponse(@JsonProperty("generated_urls") List<String> generatedUrls) {}
