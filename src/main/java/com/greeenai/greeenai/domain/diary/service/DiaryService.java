package com.greeenai.greeenai.domain.diary.service;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.diary.dto.DiaryResponse;
import com.greeenai.greeenai.domain.diary.repository.DiaryRepository;
import com.greeenai.greeenai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

	private final DiaryRepository diaryRepository;

	public DiaryResponse findDiaryById(Long id) {
		Diary diary = diaryRepository.findById(id)
				.orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
		return DiaryResponse.from(diary);
	}
}
