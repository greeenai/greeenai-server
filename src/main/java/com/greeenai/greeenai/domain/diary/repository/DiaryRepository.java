package com.greeenai.greeenai.domain.diary.repository;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByMemberAndEntryDate(Member member, LocalDate entryDate);
}
