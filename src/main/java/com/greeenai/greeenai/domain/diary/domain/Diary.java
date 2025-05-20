package com.greeenai.greeenai.domain.diary.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import com.greeenai.greeenai.domain.image.domain.Image;
import com.greeenai.greeenai.domain.member.domain.Member;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.AccessLevel;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Setter
    private String content;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", unique = true)
    private Image image;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "diary_id")
    private List<Image> userImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "diary_id")
    private List<Question> questions = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Diary(LocalDate entryDate, Member member, List<Image> userImages) {
        this.entryDate = entryDate;
        this.member = member;
        this.userImages = userImages;
        this.status = DiaryStatus.IN_PROGRESS;
    }

    public static Diary create(LocalDate entryDate, Member member, List<Image> userImages) {
        return Diary.builder()
                .entryDate(entryDate)
                .member(member)
                .userImages(userImages)
                .build();
    }

    public void addQuestions(List<Question> questions) {
        this.questions.addAll(questions);
    }

    public void updateEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public void markAsCompleted() {
        this.status = DiaryStatus.COMPLETED;
    }
}
