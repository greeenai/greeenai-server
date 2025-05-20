package com.greeenai.greeenai.domain.diary.repository;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.member.domain.Member;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;

public interface DiaryCustomRepository {
    List<Diary> findAllByMemberAndEntryDate(Member member, @Nullable LocalDate entryDate);
}
