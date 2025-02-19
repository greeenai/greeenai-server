package com.greeenai.greeenai.domain.member.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    private Long id;

    private String name;

    private String email;

    private String authId;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    private LocalDateTime lastLoginAt;
}
