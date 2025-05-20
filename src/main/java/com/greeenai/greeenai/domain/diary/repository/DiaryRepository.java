package com.greeenai.greeenai.domain.diary.repository;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.diary.domain.DiaryStatus;
import com.greeenai.greeenai.domain.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByIdAndStatus(Long id, DiaryStatus status);

    List<Diary> findAllByMemberAndEntryDate(Member member, LocalDate entryDate);
}
