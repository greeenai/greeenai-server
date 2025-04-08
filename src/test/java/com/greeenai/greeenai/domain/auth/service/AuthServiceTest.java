package com.greeenai.greeenai.domain.auth.service;

import static com.greeenai.greeenai.global.common.constant.SecurityConstants.*;
import static com.greeenai.greeenai.global.common.constant.TestConstants.*;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles({"test"})
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

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTests {

        @Test
        @DisplayName("기존 회원이 로그인 요청을 하면 성공한다")
        void login_existingMember() {
            // Given
            LoginRequest loginRequest = createLoginRequest();
            Member mockMember = mock(Member.class);

            when(jwtProperties.getToken()).thenReturn(createTokenMap());
            when(memberRepository.findByOauthId(TEST_OAUTH_ID)).thenReturn(Optional.of(mockMember));
            when(jwtService.generateToken(eq(ACCESS_TOKEN), anyLong())).thenReturn(TEST_ACCESS_TOKEN);
            when(jwtService.generateToken(eq(REFRESH_TOKEN), anyLong())).thenReturn(TEST_REFRESH_TOKEN);

            // When
            authService.login(loginRequest, response);

            // Then
            verify(mockMember).updateLastLoginAt(any(LocalDateTime.class));
            verify(memberRepository).save(mockMember);
            verifyTokenOperations();
        }

        @Test
        @DisplayName("신규 회원이 로그인 요청을 하면 회원을 생성하고 성공한다")
        void login_newMember() {
            // Given
            LoginRequest loginRequest = createLoginRequest();
            Member testMember = Member.create(
                    loginRequest.name(), loginRequest.email(), loginRequest.oauthId(), loginRequest.oAuthProvider());

            ReflectionTestUtils.setField(testMember, "id", TEST_MEMBER_ID);
            when(jwtProperties.getToken()).thenReturn(createTokenMap());
            when(memberRepository.save(any(Member.class))).thenReturn(testMember);

            ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);

            // When
            authService.login(loginRequest, response);

            // Then
            verify(memberRepository, times(2)).save(memberCaptor.capture());
            Member savedMember = memberCaptor.getValue();
            assertThat(savedMember.getId()).isEqualTo(TEST_MEMBER_ID);
            assertThat(savedMember.getName()).isEqualTo(TEST_NAME);
            assertThat(savedMember.getEmail()).isEqualTo(TEST_EMAIL);
            assertThat(savedMember.getOauthId()).isEqualTo(TEST_OAUTH_ID);
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class LogoutTests {

        @Test
        @DisplayName("로그아웃 요청을 하면 저장된 토큰을 삭제한다")
        void logout() {
            // Given
            when(memberUtil.getCurrentMemberId()).thenReturn(TEST_MEMBER_ID);

            RefreshToken refreshToken = RefreshToken.create(TEST_MEMBER_ID, TEST_REFRESH_TOKEN, TEST_EXPIRATION_TIME);
            refreshTokenRepository.save(refreshToken);

            // When
            authService.logout();

            // Then
            verify(refreshTokenRepository).deleteByMemberId(TEST_MEMBER_ID);
        }
    }

    private LoginRequest createLoginRequest() {
        return new LoginRequest(TEST_NAME, TEST_EMAIL, TEST_OAUTH_ID, OauthProvider.APPLE);
    }

    private Map<String, JwtProperties.TokenProperty> createTokenMap() {
        Map<String, JwtProperties.TokenProperty> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN, new JwtProperties.TokenProperty(TEST_SECRET, TEST_EXPIRATION_TIME));
        tokenMap.put(REFRESH_TOKEN, new JwtProperties.TokenProperty(TEST_SECRET, TEST_EXPIRATION_TIME * 24));
        return tokenMap;
    }

    private void verifyTokenOperations() {
        // Refresh Token 저장 검증
        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());
        RefreshToken savedToken = tokenCaptor.getValue();
        assertThat(savedToken.getValue()).isEqualTo(TEST_REFRESH_TOKEN);

        // 헤더 추가 검증
        verify(jwtUtil).addTokenToHeader(response, ACCESS_TOKEN, TEST_ACCESS_TOKEN);
        verify(jwtUtil).addTokenToHeader(response, REFRESH_TOKEN, TEST_REFRESH_TOKEN);
    }
}
