package com.greeenai.greeenai.domain.diary.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.diary.dto.request.AIGenerateDiaryRequest;
import com.greeenai.greeenai.domain.diary.dto.request.AIGenerateImagesRequest;
import com.greeenai.greeenai.domain.diary.dto.request.AIGenerateQuestionsRequest;
import com.greeenai.greeenai.domain.diary.dto.response.AIGenerateDiaryResponse;
import com.greeenai.greeenai.domain.diary.dto.response.AIGenerateImagesResponse;
import com.greeenai.greeenai.domain.diary.dto.response.AIGenerateQuestionsResponse;
import com.greeenai.greeenai.domain.diary.dto.response.AIQuestionResponse;
import com.greeenai.greeenai.global.error.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AIClient {

    private final WebClient webClient;

    public List<AIQuestionResponse> generateQuestions(List<String> imageUrls) {
        AIGenerateQuestionsRequest request = AIGenerateQuestionsRequest.from(imageUrls);

        AIGenerateQuestionsResponse response = webClient
                .post()
                .uri("/generate-questions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AIGenerateQuestionsResponse.class)
                .block();

        if (response == null || response.questions() == null) {
            throw new CustomException(AI_QUESTION_GENERATION_FAILED);
        }

        return response.questions();
    }

    public List<String> generateImages(List<String> imageUrls, List<String> captions) {
        AIGenerateImagesRequest request = AIGenerateImagesRequest.of(imageUrls, captions);

        AIGenerateImagesResponse response = webClient
                .post()
                .uri("/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AIGenerateImagesResponse.class)
                .block();

        if (response == null) {
            throw new CustomException(AI_IMAGE_GENERATION_FAILED);
        }

        return response.generatedUrls();
    }

    public String generateDiary(List<AIGenerateDiaryRequest.DiaryEntry> entries) {
        AIGenerateDiaryRequest request = AIGenerateDiaryRequest.of(entries);

        AIGenerateDiaryResponse response = webClient
                .post()
                .uri("/generate-diary")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AIGenerateDiaryResponse.class)
                .block();

        if (response == null || response.diary() == null) {
            throw new CustomException(AI_DIARY_GENERATION_FAILED);
        }

        return response.diary();
    }
}
