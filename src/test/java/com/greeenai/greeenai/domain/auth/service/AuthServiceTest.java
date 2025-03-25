package com.greeenai.greeenai.domain.auth.service;

import static com.greeenai.greeenai.global.common.constant.SecurityConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.greeenai.greeenai.domain.auth.domain.RefreshToken;
import com.greeenai.greeenai.domain.auth.repository.RefreshTokenRepository;
import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.domain.member.domain.OauthProvider;
import com.greeenai.greeenai.domain.member.dto.LoginRequest;
import com.greeenai.greeenai.domain.member.repository.MemberRepository;
import com.greeenai.greeenai.global.property.JwtProperties;
import com.greeenai.greeenai.global.security.JwtService;
import com.greeenai.greeenai.global.util.JwtUtil;
import com.greeenai.greeenai.global.util.MemberUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles({"test", "h2"})
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MemberUtil memberUtil;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private final String testOauthId = "testOauthId";
    private final Long testMemberId = 1L;
    private final String testAccessToken = "testAccessToken";
    private final String testRefreshToken = "testRefreshToken";
    private final String testSecret = "testSecret";
    private final long testExpirationTime = 3600L;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("Test User", "test@email.com", testOauthId, OauthProvider.APPLE);
    }

    @Test
    @DisplayName("로그인 성공 - 기존 회원 조회")
    void login_existingMember() {
        // Given
        Member mockMember = mock(Member.class);

        when(jwtProperties.getToken()).thenReturn(createTokenMap());
        when(memberRepository.findByOauthId(testOauthId)).thenReturn(Optional.of(mockMember));
        when(jwtService.generateToken(eq(ACCESS_TOKEN), anyLong())).thenReturn(testAccessToken);
        when(jwtService.generateToken(eq(REFRESH_TOKEN), anyLong())).thenReturn(testRefreshToken);

        // When
        authService.login(loginRequest, response);

        // Then
        verify(mockMember).updateLastLoginAt(any(LocalDateTime.class));
        verify(memberRepository).save(mockMember);
        verifyTokenOperations();
    }

    @Test
    @DisplayName("로그인 성공 - 신규 회원 생성")
    void login_newMember() {
        // Given
        Member testMember = Member.create(
                loginRequest.name(), loginRequest.email(), loginRequest.oauthId(), loginRequest.oAuthProvider());

        ReflectionTestUtils.setField(testMember, "id", testMemberId);
        when(jwtProperties.getToken()).thenReturn(createTokenMap());
        when(memberRepository.findByOauthId(testOauthId)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);
        when(jwtService.generateToken(eq(ACCESS_TOKEN), eq(testMemberId))).thenReturn(testAccessToken);
        when(jwtService.generateToken(eq(REFRESH_TOKEN), eq(testMemberId))).thenReturn(testRefreshToken);

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);

        // When
        authService.login(loginRequest, response);

        // Then
        verify(memberRepository, times(2)).save(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember.getOauthId()).isEqualTo(testOauthId);
        verifyTokenOperations();
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout() {
        // Given
        when(memberUtil.getCurrentMemberId()).thenReturn(testMemberId);

        // When
        authService.logout();

        // Then
        verify(refreshTokenRepository).deleteByMemberId(testMemberId);
    }

    private Map<String, JwtProperties.TokenProperty> createTokenMap() {
        Map<String, JwtProperties.TokenProperty> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN, new JwtProperties.TokenProperty(testSecret, testExpirationTime));
        tokenMap.put(REFRESH_TOKEN, new JwtProperties.TokenProperty(testSecret, testExpirationTime * 24));
        return tokenMap;
    }

    private void verifyTokenOperations() {
        // Refresh Token 저장 검증
        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());
        RefreshToken savedToken = tokenCaptor.getValue();
        assertThat(savedToken.getValue()).isEqualTo(testRefreshToken);

        // 헤더 추가 검증
        verify(jwtUtil).addTokenToHeader(response, ACCESS_TOKEN, testAccessToken);
        verify(jwtUtil).addTokenToHeader(response, REFRESH_TOKEN, testRefreshToken);
    }
}
