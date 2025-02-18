package com.greeenai.greeenai.domain.auth;

import static com.greeenai.greeenai.global.common.SecurityConstants.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.domain.member.dto.LoginRequest;
import com.greeenai.greeenai.domain.member.repository.MemberRepository;
import com.greeenai.greeenai.global.security.JwtService;
import com.greeenai.greeenai.global.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

	private final JwtUtil jwtUtil;
	private final JwtService jwtService;
	private final MemberRepository memberRepository;

	@Transactional
	public void login(LoginRequest request, HttpServletResponse response) {
		Member member = findOrCreate(request);
		String accessToken = jwtService.generateToken(ACCESS_TOKEN, member.getId());
		String refreshToken = jwtService.generateToken(REFRESH_TOKEN, member.getId());

		// TODO : redis에 refreshToken 저장

		jwtUtil.addTokenCookie(response, ACCESS_TOKEN, accessToken);
		jwtUtil.addTokenCookie(response, REFRESH_TOKEN, refreshToken);

		member.updateLastLoginAt(LocalDateTime.now());
		memberRepository.save(member);

		log.info("[LoginService] 로그인 성공 : memberId={}", member.getId());
	}

	private Member findOrCreate(LoginRequest request) {
		return memberRepository.findByAuthId(request.oAuthId())
			.orElse(memberRepository.save(
				Member.create(request.name(), request.email(), request.oAuthId(), request.oAuthProvider())));
	}
}
