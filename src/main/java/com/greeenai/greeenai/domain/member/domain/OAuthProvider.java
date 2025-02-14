package com.greeenai.greeenai.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuthProvider {
	APPLE("apple"),
	KAKAO("kakao");

	private final String value;
}
