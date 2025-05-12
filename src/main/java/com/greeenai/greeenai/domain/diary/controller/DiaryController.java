package com.greeenai.greeenai.domain.diary.controller;

import com.greeenai.greeenai.domain.diary.dto.request.DiaryUpdateRequest;
import com.greeenai.greeenai.domain.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

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
