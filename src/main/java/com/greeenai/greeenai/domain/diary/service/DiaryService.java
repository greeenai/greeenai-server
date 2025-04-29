package com.greeenai.greeenai.domain.diary.service;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.diary.dto.DiaryCreateRequest;
import com.greeenai.greeenai.domain.diary.dto.DiaryResponse;
import com.greeenai.greeenai.domain.diary.dto.DiaryWithQuestionsResponse;
import com.greeenai.greeenai.domain.diary.repository.DiaryRepository;
import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.util.MemberUtil;
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
	private final MemberUtil memberUtil;

	public DiaryResponse findDiaryById(Long id) {
		Diary diary = diaryRepository.findById(id)
				.orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
		return DiaryResponse.from(diary);
	}

	@Transactional
	public DiaryWithQuestionsResponse createDiary(DiaryCreateRequest request) {
		Member currentMember = memberUtil.getCurrentMember();
		Diary diary = Diary.create(null, request.entryDate(), currentMember);
		diaryRepository.save(diary);
		// TODO : AI에게 그림일기 생성용 사진 주고 질문 받아오기
		return DiaryWithQuestionsResponse.from(diary);
	}
}
