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

    private String content;

    @Column(nullable = false)
    private LocalDate entryDate;

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

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Diary(String content, LocalDate entryDate, Member member, List<Image> userImages) {
        this.content = content;
        this.entryDate = entryDate;
        this.member = member;
        this.userImages = userImages;
    }

    public static Diary create(String content, LocalDate entryDate, Member member, List<Image> userImages) {
        return Diary.builder()
                .content(content)
                .entryDate(entryDate)
                .member(member)
                .userImages(userImages)
                .build();
    }

    public void updateEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
}
