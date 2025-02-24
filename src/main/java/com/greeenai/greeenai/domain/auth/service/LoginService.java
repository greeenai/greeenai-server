package com.greeenai.greeenai.domain.auth.service;

import static com.greeenai.greeenai.global.common.SecurityConstants.*;

import com.greeenai.greeenai.domain.auth.domain.RefreshToken;
import com.greeenai.greeenai.domain.auth.repository.RefreshTokenRepository;
import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.domain.member.dto.LoginRequest;
import com.greeenai.greeenai.domain.member.repository.MemberRepository;
import com.greeenai.greeenai.global.property.JwtProperties;
import com.greeenai.greeenai.global.security.JwtService;
import com.greeenai.greeenai.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void login(LoginRequest request, HttpServletResponse response) {
        Member member = findOrCreate(request);
        String accessToken = jwtService.generateToken(ACCESS_TOKEN, member.getId());
        String refreshToken = jwtService.generateToken(REFRESH_TOKEN, member.getId());

        long ttl = jwtProperties.getToken().get(REFRESH_TOKEN).expirationMilliTime();
        refreshTokenRepository.save(RefreshToken.create(member.getId(), refreshToken, ttl));

        jwtUtil.addTokenToHeader(response, ACCESS_TOKEN, accessToken);
        jwtUtil.addTokenToHeader(response, REFRESH_TOKEN, refreshToken);

        member.updateLastLoginAt(LocalDateTime.now());
        memberRepository.save(member);

        log.info("[LoginService] 로그인 성공 : memberId={}", member.getId());
    }

    private Member findOrCreate(LoginRequest request) {
        return memberRepository
                .findByOauthId(request.oauthId())
                .orElse(memberRepository.save(
                        Member.create(request.name(), request.email(), request.oauthId(), request.oAuthProvider())));
    }
}
