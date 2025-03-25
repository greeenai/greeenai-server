package com.greeenai.greeenai.global.security;

import static com.greeenai.greeenai.global.common.constant.SecurityConstants.*;
import static com.greeenai.greeenai.global.error.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.greeenai.greeenai.global.error.exception.CustomException;
import com.greeenai.greeenai.global.property.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test", "h2"})
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    private final Long testMemberId = 1L;
    private final String testSecret = "testSecretKeyWithAtLeast32Characters";
    private final long testExpirationTime = 3600L;

    @BeforeEach
    void setUp() {
        //		when(jwtProperties.getToken()).thenReturn(createTokenMap());
    }

    @Test
    @DisplayName("토큰 생성 성공")
    void generateToken_shouldCreateValidToken() {
        // Given
        when(jwtProperties.getToken()).thenReturn(createTokenMap());

        // When
        String token = jwtService.generateToken(ACCESS_TOKEN, testMemberId);

        // Then
        assertThat(token).isNotEmpty();
        assertThat(Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(testSecret.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject())
                .isEqualTo(testMemberId.toString());
    }

    @Test
    @DisplayName("토큰 파싱 성공")
    void parseToken_shouldReturnMemberId() {
        // Given
        when(jwtProperties.getToken()).thenReturn(createTokenMap());
        String token = jwtService.generateToken(ACCESS_TOKEN, testMemberId);

        // When
        Long parsedMemberId = jwtService.parseToken(ACCESS_TOKEN, token);

        // Then
        assertThat(parsedMemberId).isEqualTo(testMemberId);
    }

    @Test
    @DisplayName("null 토큰 파싱 시 예외 발생")
    void parseToken_withNullToken_shouldThrowException() {
        // Given
        // 스터빙이 필요 없음

        // When & Then
        assertThatThrownBy(() -> jwtService.parseToken(ACCESS_TOKEN, null))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("잘못된 토큰 타입으로 토큰 생성 시 예외 발생")
    void generateToken_withInvalidTokenType_shouldThrowException() {
        // Given
        // 스터빙이 필요 없음

        // When & Then
        assertThatThrownBy(() -> jwtService.generateToken("INVALID_TOKEN_TYPE", 1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(TOKEN_TYPE_INVALID.getMessage());
    }

    @Test
    @DisplayName("잘못된 토큰 타입으로 토큰 파싱 시 예외 발생")
    void parseToken_withInvalidTokenType_shouldThrowException() {
        // Given
        when(jwtProperties.getToken()).thenReturn(createTokenMap());
        String token = jwtService.generateToken(ACCESS_TOKEN, 1L);

        // When & Then
        assertThatThrownBy(() -> jwtService.parseToken("INVALID_TOKEN_TYPE", token))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(TOKEN_TYPE_INVALID.getMessage());
    }

    private Map<String, JwtProperties.TokenProperty> createTokenMap() {
        Map<String, JwtProperties.TokenProperty> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN, new JwtProperties.TokenProperty(testSecret, testExpirationTime));
        tokenMap.put(REFRESH_TOKEN, new JwtProperties.TokenProperty(testSecret, testExpirationTime * 24));
        return tokenMap;
    }
}
