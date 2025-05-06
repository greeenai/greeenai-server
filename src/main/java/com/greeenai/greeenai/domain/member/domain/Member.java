package com.greeenai.greeenai.domain.member.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import com.greeenai.greeenai.domain.diary.domain.Diary;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"oauthId", "oauthProvider"})})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String oauthId;

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    private LocalDateTime lastLoginAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(String name, String email, String oauthId, OauthProvider oauthProvider) {
        this.name = name;
        this.email = email;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
    }

    public static Member create(String name, String email, String oauthId, OauthProvider oauthProvider) {
        return Member.builder()
                .name(name)
                .email(email)
                .oauthId(oauthId)
                .oauthProvider(oauthProvider)
                .build();
    }

    public void updateLastLoginAt(LocalDateTime now) {
        this.lastLoginAt = now;
    }
}
