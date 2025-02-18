package com.greeenai.greeenai.domain.member.domain;

import java.time.LocalDateTime;

import com.greeenai.greeenai.domain.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"oAuthId", "oAuthProvider"})})
public class Member extends BaseEntity {

	@Id
	private Long id;

	private String name;

	private String email;

	private String oAuthId;

	@Enumerated(EnumType.STRING)
	private OAuthProvider oAuthProvider;

	private LocalDateTime lastLoginAt;

	@Builder(access = AccessLevel.PRIVATE)
	private Member(String name, String email, String oAuthId, OAuthProvider oAuthProvider) {
		this.name = name;
		this.email = email;
		this.oAuthId = oAuthId;
		this.oAuthProvider = oAuthProvider;
	}

	public static Member create(String name, String email, String oAuthId, OAuthProvider oAuthProvider) {
		return Member.builder()
				.name(name)
				.email(email)
				.oAuthId(oAuthId)
				.oAuthProvider(oAuthProvider)
				.build();
	}

	public void updateLastLoginAt(LocalDateTime now) {
		this.lastLoginAt = now;
	}
}
