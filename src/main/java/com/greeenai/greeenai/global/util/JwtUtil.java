package com.greeenai.greeenai.global.util;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

	// cookie로부터 token을 추출합니다.
	public String extractTokenFromCookie(HttpServletRequest request, String tokenType) {
		return Optional.ofNullable(request.getCookies())
			.flatMap(cookies -> Arrays.stream(cookies)
				.filter(cookie -> tokenType.equals(cookie.getName()))
				.findFirst()
				.map(Cookie::getValue))
			.orElse(null);
	}

	// token cookie를 response header에 추가합니다.
	public void addTokenCookie(HttpServletResponse response, String tokenType, String token) {
		ResponseCookie tokenCookie = ResponseCookie.from(tokenType, token)
			.path("/")
			.secure(true)
			.sameSite("Lax")
			.httpOnly(true)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
	}
}
