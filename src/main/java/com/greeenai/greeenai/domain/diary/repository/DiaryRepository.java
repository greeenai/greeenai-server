package com.greeenai.greeenai.domain.diary.repository;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
