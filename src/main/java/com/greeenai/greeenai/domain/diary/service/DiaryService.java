package com.greeenai.greeenai.domain.diary.service;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;

import com.greeenai.greeenai.domain.ai.dto.request.DiaryEntry;
import com.greeenai.greeenai.domain.ai.dto.response.GenerateQuestionsResponse.GeneratedQuestion;
import com.greeenai.greeenai.domain.ai.dto.response.GeneratedImage;
import com.greeenai.greeenai.domain.ai.service.AIClient;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final AIClient aiClient;
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
    public DownloadUrlResponse getDownloadUrlByDiaryId(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        String downloadUrl =
                imageService.generateImageDownloadUrl(diary.getImage().getId());
        return DownloadUrlResponse.from(downloadUrl);
    }

    @Transactional
    public DiaryWithQuestionsResponse createDiary(DiaryCreateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        List<Image> userImages = saveUserImages(request.getPhotos());
        Diary diary = Diary.create(request.getEntryDate(), currentMember, userImages);
        diaryRepository.save(diary);

        List<String> userImageUrls =
                userImages.stream().map(imageService::getUrl).toList();
        List<GeneratedQuestion> generatedQuestions = aiClient.generateQuestions(userImageUrls);
        List<Question> questions = createQuestions(generatedQuestions, diary);
        questionRepository.saveAll(questions);

        log.info("[DiaryService] 일기 생성 성공 : diaryId={}", diary.getId());
        return DiaryWithQuestionsResponse.from(diary);
    }

    @Transactional
    public DiaryResponse answerDiaryQuestions(Long diaryId, List<QuestionAnswerRequest> requests) {
        List<DiaryEntry> entries = toDiaryEntries(requests);
        GeneratedImage generatedImage = aiClient.generateImage(entries);
        String diaryContent = aiClient.generateDiary(entries);

        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        Image diaryImage = saveDiaryImage(diary, generatedImage);
        diary.setImage(diaryImage);
        diary.setContent(diaryContent);
        diary.markAsCompleted();

        log.info("[DiaryService] 질문 답변 성공 : diaryId={}", diaryId);
        return DiaryResponse.of(diary, getDiaryImageUrl(diary));
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

    private Image saveDiaryImage(Diary diary, GeneratedImage generatedImage) {
        return imageService.uploadImage(
                generatedImage.bytes(), generatedImage.contentType(), ImageType.DIARY, diary.getId());
    }

    private List<Question> createQuestions(List<GeneratedQuestion> generatedQuestions, Diary diary) {
        return generatedQuestions.stream()
                .map(question -> Question.create(
                        question.title(),
                        question.caption(),
                        question.prompt(),
                        diary,
                        question.options().stream().map(Option::create).toList()))
                .toList();
    }

    private List<DiaryEntry> toDiaryEntries(List<QuestionAnswerRequest> requests) {
        return requests.stream()
                .map(request -> {
                    Question question = questionRepository
                            .findById(request.questionId())
                            .orElseThrow(() -> new CustomException(QUESTION_NOT_FOUND));
                    return DiaryEntry.of(question, request.answerContent());
                })
                .toList();
    }
}
