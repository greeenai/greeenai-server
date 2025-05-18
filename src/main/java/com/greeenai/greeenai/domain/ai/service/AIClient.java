package com.greeenai.greeenai.domain.ai.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.ai.dto.request.DiaryEntry;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateDiaryRequest;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateImageRequest;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateQuestionsRequest;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateDiaryResponse;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateQuestionsResponse;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateQuestionsResponse.GeneratedQuestion;
import com.greeenai.greeenai.domain.ai.dto.response.GeneratedImage;
import com.greeenai.greeenai.global.error.exception.CustomException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AIClient {

    private final WebClient webClient;

    public List<GeneratedQuestion> generateQuestions(List<String> imageUrls) {
        GenerateQuestionsRequest request = GenerateQuestionsRequest.from(imageUrls);
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            GenerateQuestionsResponse response;
            try {
                response = webClient
                        .post()
                        .uri("/generate-question")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(GenerateQuestionsResponse.class)
                        .block();
            } catch (Exception e) {
                throw new CustomException(AI_QUESTION_GENERATION_FAILED); // 예외는 즉시 중단
            }

            if (isValidQuestionsResponse(response)) {
                return response.questions(); // 유효 응답
            }

            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(1000); // 다음 시도까지 대기
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new CustomException(AI_QUESTION_GENERATION_FAILED);
                }
            }
        }

        throw new CustomException(AI_QUESTION_GENERATION_FAILED);
    }

    public GeneratedImage generateImage(List<DiaryEntry> entries) {
        GenerateImageRequest request = GenerateImageRequest.of(entries);

        ResponseEntity<byte[]> response = webClient
                .post()
                .uri("/generate-image")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .toEntity(byte[].class)
                .block();

        if (response == null || response.getBody() == null) {
            throw new CustomException(AI_IMAGE_GENERATION_FAILED);
        }

        return GeneratedImage.of(
                response.getBody(),
                Optional.ofNullable(response.getHeaders().getContentType())
                        .map(MediaType::toString)
                        .orElse("image/png"));
    }

    public String generateDiary(List<DiaryEntry> entries) {
        GenerateDiaryRequest request = GenerateDiaryRequest.of(entries);

        GenerateDiaryResponse response = webClient
                .post()
                .uri("/generate-diary")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateDiaryResponse.class)
                .block();

        if (response == null || response.diary() == null) {
            throw new CustomException(AI_DIARY_GENERATION_FAILED);
        }

        return response.diary();
    }

    private boolean isValidQuestionsResponse(GenerateQuestionsResponse response) {
        if (response == null
                || response.questions() == null
                || response.questions().isEmpty()) {
            return false;
        }

        for (GenerateQuestionsResponse.GeneratedQuestion q : response.questions()) {
            if (q == null
                    || q.title() == null
                    || q.caption() == null
                    || q.prompt() == null
                    || q.options() == null
                    || q.options().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
