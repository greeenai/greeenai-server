package com.greeenai.greeenai.domain.diary.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.diary.domain.Option;
import com.greeenai.greeenai.domain.diary.domain.Question;
import com.greeenai.greeenai.domain.diary.dto.request.*;
import com.greeenai.greeenai.domain.diary.dto.response.*;
import com.greeenai.greeenai.domain.diary.repository.DiaryRepository;
import com.greeenai.greeenai.domain.diary.repository.OptionRepository;
import com.greeenai.greeenai.domain.diary.repository.QuestionRepository;
import com.greeenai.greeenai.domain.image.domain.Image;
import com.greeenai.greeenai.domain.image.domain.ImageType;
import com.greeenai.greeenai.domain.image.service.ImageService;
import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.util.MemberUtil;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final ImageService imageService;
    private final DiaryRepository diaryRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final MemberUtil memberUtil;

    @Transactional(readOnly = true)
    public DiaryResponse findDiaryById(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        return DiaryResponse.of(diary, getDiaryImageUrl(diary));
    }

	@Transactional(readOnly = true)
	public List<DiaryResponse> findAllMyDiaries(@Nullable LocalDate entryDate) {
		Member currentMember = memberUtil.getCurrentMember();
		List<Diary> myDiaries = diaryRepository.findAllByMemberAndEntryDate(currentMember, entryDate);

		return myDiaries.stream()
				.map(diary -> DiaryResponse.of(diary, getDiaryImageUrl(diary)))
				.toList();
	}

    @Transactional(readOnly = true)
    public String getDownloadUrlByDiaryId(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        return imageService.generateImageDownloadUrl(diary.getImage().getId());
    }

    @Transactional
    public DiaryWithQuestionsResponse createDiary(DiaryCreateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        List<Image> userImages = saveUserImages(request.getPhotos());
        Diary diary = Diary.create(null, request.getEntryDate(), currentMember, userImages);
        diaryRepository.save(diary);

        List<String> userImageUrls =
                userImages.stream().map(imageService::getUrl).toList();
        List<QuestionResponse> questionResponses = generateQuestionsFromAI(userImageUrls);
        List<Question> questions = questionResponses.stream()
                .map(qr -> Question.create(
                        qr.title(),
                        qr.caption(),
                        qr.prompt(),
                        diary,
                        qr.options().stream()
                                .map(opt -> Option.create(opt.content(), false))
                                .toList()))
                .toList();

        questionRepository.saveAll(questions);

        log.info("[DiaryService] 일기 생성 성공 : diaryId={}", diary.getId());
        return DiaryWithQuestionsResponse.of(diary, getDiaryImageUrl(diary));
    }

    @Transactional
    public DiaryWithQuestionsAndAnswersResponse answerDiaryQuestions(
            Long diaryId, List<QuestionAnswerRequest> requests) {
        requests.forEach(request -> {
            Option option = optionRepository
                    .findById(request.optionId())
                    .orElseThrow(() -> new CustomException(OPTION_NOT_FOUND));
            option.markAsAnswer();
        });
        // TODO : AI에게 질문 답변 묶음 보내주기
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        log.info("[DiaryService] 질문 답변 성공 : diaryId={}", diaryId);
        return DiaryWithQuestionsAndAnswersResponse.of(diary, getDiaryImageUrl(diary));
    }

    @Transactional
    public void updateDiaryEntryDate(Long diaryId, DiaryUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        validateDiaryOwner(currentMember.getId(), diary.getMember().getId());

        diary.updateEntryDate(request.entryDate());
        diaryRepository.save(diary);
        log.info("[DiaryService] 일기 날짜 수정 성공 : diaryId={}", diaryId);
    }

    @Transactional
    public void deleteDiary(Long diaryId) {
        Member currentMember = memberUtil.getCurrentMember();
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        validateDiaryOwner(currentMember.getId(), diary.getMember().getId());

        diaryRepository.delete(diary);
        log.info("[DiaryService] 일기 삭제 성공 : diaryId={}", diaryId);
    }

    private void validateDiaryOwner(Long currentMemberId, Long diaryOwnerId) {
        if (!currentMemberId.equals(diaryOwnerId)) {
            throw new CustomException(DIARY_OWNER_MISMATCH);
        }
    }

    private String getDiaryImageUrl(Diary diary) {
        return imageService.getUrl(diary.getImage());
    }

    private List<Image> saveUserImages(List<MultipartFile> userImages) {
        Member currentMember = memberUtil.getCurrentMember();
        return userImages.stream()
                .map(userImage -> imageService.uploadImage(userImage, ImageType.USER, currentMember.getId()))
                .toList();
    }

    private List<QuestionResponse> generateQuestionsFromAI(List<String> imageUrls) {
        GenerateQuestionsRequest aiRequest = GenerateQuestionsRequest.from(imageUrls);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GenerateQuestionsRequest> entity = new HttpEntity<>(aiRequest, headers);

        ResponseEntity<GenerateQuestionsResponse> aiResponse = restTemplate.postForEntity(
                "https://kaggom.online/generate-questions", // TODO: 유효한 URL로 변경
                entity,
                GenerateQuestionsResponse.class);

        return aiResponse.getBody().questions();
    }
}
