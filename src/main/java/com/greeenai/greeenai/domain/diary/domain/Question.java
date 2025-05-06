package com.greeenai.greeenai.domain.diary.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Answer answer;

    @Builder(access = AccessLevel.PRIVATE)
    private Question(String content, Diary diary) {
        this.content = content;
        this.diary = diary;
    }

    public static Question create(String content, Diary diary) {
        return Question.builder().content(content).diary(diary).build();
    }
}
