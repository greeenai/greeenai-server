package com.greeenai.greeenai.domain.ai.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.ai.dto.request.GenerateDiaryRequest;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateDiaryRequest.DiaryEntry;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateImagesRequest;
import com.greeenai.greeenai.domain.ai.dto.request.GenerateQuestionsRequest;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateDiaryResponse;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateImagesResponse;
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
                .uri("/generate-questions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateQuestionsResponse.class)
                .block();

        if (response == null || response.questions() == null) {
            throw new CustomException(AI_QUESTION_GENERATION_FAILED);
        }

        return response.questions();
    }

    public List<String> generateImages(List<String> imageUrls, List<String> captions) {
        GenerateImagesRequest request = GenerateImagesRequest.of(imageUrls, captions);

        GenerateImagesResponse response = webClient
                .post()
                .uri("/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GenerateImagesResponse.class)
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
