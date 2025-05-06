package com.greeenai.greeenai.domain.diary.repository;

import com.greeenai.greeenai.domain.diary.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {}
