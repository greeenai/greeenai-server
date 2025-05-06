package com.greeenai.greeenai.domain.diary.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.diary.domain.Answer;
import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.diary.domain.Question;
import com.greeenai.greeenai.domain.diary.dto.request.DiaryCreateRequest;
import com.greeenai.greeenai.domain.diary.dto.request.QuestionAnswerRequest;
import com.greeenai.greeenai.domain.diary.dto.response.DiaryResponse;
import com.greeenai.greeenai.domain.diary.dto.response.DiaryWithQuestionsAndAnswersResponse;
import com.greeenai.greeenai.domain.diary.dto.response.DiaryWithQuestionsResponse;
import com.greeenai.greeenai.domain.diary.repository.AnswerRepository;
import com.greeenai.greeenai.domain.diary.repository.DiaryRepository;
import com.greeenai.greeenai.domain.diary.repository.QuestionRepository;
import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.util.MemberUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MemberUtil memberUtil;

    @Transactional(readOnly = true)
    public DiaryResponse findDiaryById(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        return DiaryResponse.from(diary);
    }

    @Transactional
    public DiaryWithQuestionsResponse createDiary(DiaryCreateRequest request) {
        log.info("createDiary 시작 - entryDate: {}", request.entryDate());
        Member currentMember = memberUtil.getCurrentMember();
        Diary diary = Diary.create(null, request.entryDate(), currentMember);
        diaryRepository.save(diary);
        // TODO : AI에게 그림일기 생성용 사진 주고 질문 받아오기
        log.info("createDiary 완료 - diaryId: {}", diary.getId());
        return DiaryWithQuestionsResponse.from(diary);
    }

    @Transactional
    public DiaryWithQuestionsAndAnswersResponse answerDiaryQuestions(
            Long diaryId, List<QuestionAnswerRequest> requests) {
        log.info("answerDiaryQuestions 시작 - diaryId: {}, 답변 개수: {}", diaryId, requests.size());
        requests.forEach(request -> {
            Question question = questionRepository
                    .findById(request.questionId())
                    .orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND));
            Answer answer = Answer.create(request.answerContent(), question);
            answerRepository.save(answer);
        });
        // TODO : AI에게 질문 답변 묶음 보내주기
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        log.info("answerDiaryQuestions 완료 - diaryId: {}", diaryId);
        return DiaryWithQuestionsAndAnswersResponse.from(diary);
    }
}
