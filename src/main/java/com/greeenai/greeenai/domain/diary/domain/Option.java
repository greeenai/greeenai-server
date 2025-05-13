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
public class Option extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private boolean isAnswer;

    @Builder(access = AccessLevel.PRIVATE)
    private Option(String content, boolean isAnswer) {
        this.content = content;
        this.isAnswer = isAnswer;
    }

    public static Option create(String content, boolean isAnswer) {
        return Option.builder().content(content).isAnswer(isAnswer).build();
    }

    public void markAsAnswer() {
        this.isAnswer = true;
    }
}
