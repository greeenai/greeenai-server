package com.greeenai.greeenai.domain.diary.controller;

import com.greeenai.greeenai.domain.diary.dto.request.DiaryCreateRequest;
import com.greeenai.greeenai.domain.diary.dto.request.DiaryUpdateRequest;
import com.greeenai.greeenai.domain.diary.dto.request.QuestionAnswerRequest;
import com.greeenai.greeenai.domain.diary.dto.response.DiaryResponse;
import com.greeenai.greeenai.domain.diary.dto.response.DiaryWithQuestionsAndAnswersResponse;
import com.greeenai.greeenai.domain.diary.dto.response.DiaryWithQuestionsResponse;
import com.greeenai.greeenai.domain.diary.service.DiaryService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping
    public ResponseEntity<List<DiaryResponse>> getMyDiaries(@RequestParam(required = false) LocalDate entryDate) {
        List<DiaryResponse> responses = diaryService.findAllMyDiaries(entryDate);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<DiaryWithQuestionsResponse> createDiary(@Valid @ModelAttribute DiaryCreateRequest request) {
        DiaryWithQuestionsResponse response = diaryService.createDiary(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiary(@PathVariable Long diaryId) {
        DiaryResponse response = diaryService.findDiaryById(diaryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{diaryId}/download-url")
    public ResponseEntity<String> getDiaryDownloadUrl(@PathVariable Long diaryId) {
        String downloadUrl = diaryService.getDownloadUrlByDiaryId(diaryId);
        return ResponseEntity.ok(downloadUrl);
    }

    @PutMapping("/{diaryId}/generate-image")
    public ResponseEntity<DiaryWithQuestionsAndAnswersResponse> generateImageWithAnswers(
            @PathVariable Long diaryId, @Valid @RequestBody List<QuestionAnswerRequest> requests) {
        DiaryWithQuestionsAndAnswersResponse response = diaryService.answerDiaryQuestions(diaryId, requests);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiaryEntryDate(
            @PathVariable Long diaryId, @Valid @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiaryEntryDate(diaryId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
}
