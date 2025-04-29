package com.greeenai.greeenai.domain.diary.service;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.diary.dto.DiaryCreateRequest;
import com.greeenai.greeenai.domain.diary.dto.DiaryResponse;
import com.greeenai.greeenai.domain.diary.repository.DiaryRepository;
import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

	private final DiaryRepository diaryRepository;
	private final MemberUtil memberUtil;

	public List<DiaryResponse> findAllDiaries() {
		Member currentMember = memberUtil.getCurrentMember();
		return currentMember.getDiaries().stream()
				.map(DiaryResponse::from)
				.toList();
	}

	public DiaryResponse findDiaryById(Long id) {
		Diary diary = diaryRepository.findById(id)
				.orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
		return DiaryResponse.from(diary);
	}

	@Transactional
	public DiaryResponse createDiary(DiaryCreateRequest request) {
		Member currentMember = memberUtil.getCurrentMember();
		Diary diary = Diary.create(null, request.entryDate(), currentMember);
		diaryRepository.save(diary);
		return DiaryResponse.from(diary);
	}

	@Transactional
	public void deleteDiaryById(Long id) {
		diaryRepository.deleteById(id);
	}
}
