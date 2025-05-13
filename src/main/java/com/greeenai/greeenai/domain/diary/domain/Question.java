package com.greeenai.greeenai.domain.diary.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    private String title;
    private String caption;
    private String prompt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id")
    private List<Option> options = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Question(String title, String caption, String prompt, Diary diary, List<Option> options) {
        this.title = title;
        this.caption = caption;
        this.prompt = prompt;
        this.diary = diary;
        this.options = options;
    }

    public static Question create(String title, String caption, String prompt, Diary diary, List<Option> options) {
        return Question.builder()
                .title(title)
                .caption(caption)
                .prompt(prompt)
                .diary(diary)
                .options(options)
                .build();
    }
}
