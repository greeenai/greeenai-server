package com.greeenai.greeenai.infra.ai.client;

import static com.greeenai.greeenai.global.common.constant.AiApiUriConstants.*;
import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.infra.ai.dto.request.DiaryEntry;
import com.greeenai.greeenai.infra.ai.dto.request.GenerateDiaryRequest;
import com.greeenai.greeenai.infra.ai.dto.request.GenerateQuestionsRequest;
import com.greeenai.greeenai.infra.ai.dto.response.GenerateDiaryResponse;
import com.greeenai.greeenai.infra.ai.dto.response.GenerateQuestionsResponse;
import com.greeenai.greeenai.infra.ai.dto.response.GenerateQuestionsResponse.GeneratedQuestion;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KaggomAIClient {

    private final WebClient kaggomWebClient;

    public List<GeneratedQuestion> generateQuestions(List<String> imageUrls) {
        GenerateQuestionsRequest request = GenerateQuestionsRequest.from(imageUrls);
        int maxAttempts = 5;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            GenerateQuestionsResponse response;
            try {
                response = kaggomWebClient
                        .post()
                        .uri(GENERATE_QUESTIONS)
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
                waitBeforeRetry();
            }
        }

        throw new CustomException(AI_QUESTION_GENERATION_FAILED);
    }

    public String generateDiary(List<DiaryEntry> entries) {
        GenerateDiaryRequest request = GenerateDiaryRequest.of(entries);

        GenerateDiaryResponse response = kaggomWebClient
                .post()
                .uri(GENERATE_DIARY)
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

    private void waitBeforeRetry() {
        try {
            Thread.sleep(1000); // 1초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 복원
            throw new CustomException(AI_QUESTION_GENERATION_FAILED);
        }
    }
}
