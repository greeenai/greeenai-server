package com.greeenai.greeenai.infra.ai.client;

import static com.greeenai.greeenai.global.common.constant.AiApiUriConstants.*;
import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.infra.ai.dto.request.DiaryEntry;
import com.greeenai.greeenai.infra.ai.dto.request.GenerateImageRequest;
import com.greeenai.greeenai.infra.ai.dto.response.GeneratedImage;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ImageAIClient {

    private final WebClient aiWebClient;

    public GeneratedImage generateImage(List<DiaryEntry> entries) {
        GenerateImageRequest request = GenerateImageRequest.of(entries);

        ResponseEntity<byte[]> response = aiWebClient
                .post()
                .uri(GENERATE_IMAGE)
                .bodyValue(request)
                .retrieve()
                .toEntity(byte[].class)
                .block();

        validateImageResponse(response);

        return GeneratedImage.of(
                response.getBody(),
                Optional.ofNullable(response.getHeaders().getContentType())
                        .map(MediaType::toString)
                        .orElse("image/png"));
    }

    private void validateImageResponse(ResponseEntity<byte[]> response) {
        if (response == null || response.getBody() == null) {
            throw new CustomException(AI_IMAGE_GENERATION_FAILED);
        }
    }
}
