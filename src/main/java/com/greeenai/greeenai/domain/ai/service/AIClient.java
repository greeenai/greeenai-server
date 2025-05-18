package com.greeenai.greeenai.domain.ai.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.ai.dto.request.DiaryEntry;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateDiaryRequest;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateImageRequest;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateQuestionsRequest;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateDiaryResponse;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateImageResponse;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateQuestionsResponse;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateQuestionsResponse.GeneratedQuestion;
import com.greeenai.greeenai.global.error.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AIClient {

    private final WebClient webClient;

    public List<GeneratedQuestion> generateQuestions(List<String> imageUrls) {
        GenerateQuestionsRequest request = GenerateQuestionsRequest.from(imageUrls);

        GenerateQuestionsResponse response = webClient
                .post()
                .uri("/generate-question")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateQuestionsResponse.class)
                .block();

        if (response == null || response.questions() == null) {
            throw new CustomException(AI_QUESTION_GENERATION_FAILED);
        }

        return response.questions();
    }

    public List<String> generateImages(List<DiaryEntry> entries) {
        GenerateImageRequest request = GenerateImageRequest.of(entries);

        GenerateImageResponse response = webClient
                .post()
                .uri("/generate-image")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateImageResponse.class)
                .block();

        if (response == null) {
            throw new CustomException(AI_IMAGE_GENERATION_FAILED);
        }

        return response.generatedUrls();
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
}
