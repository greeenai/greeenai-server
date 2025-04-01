package com.greeenai.greeenai.domain.auth.controller;

import static com.greeenai.greeenai.global.common.constant.TestConstants.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greeenai.greeenai.domain.auth.service.AuthService;
import com.greeenai.greeenai.domain.member.domain.OauthProvider;
import com.greeenai.greeenai.domain.member.dto.LoginRequest;
import com.greeenai.greeenai.global.config.SecurityConfig;
import com.greeenai.greeenai.global.security.JwtService;
import com.greeenai.greeenai.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles({"test"})
@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유효한 로그인 요청을 하면 200 OK를 반환한다")
    void login_shouldReturnOk() throws Exception {
        // Given
        LoginRequest request = new LoginRequest(TEST_NAME, TEST_EMAIL, TEST_OAUTH_ID, OauthProvider.APPLE);

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService, times(1)).login(eq(request), any());
    }

    @Test
    @DisplayName("유효하지 않은 로그인 요청을 하면 400 Bad Request를 반환한다")
    void login_shouldReturnBadRequest_whenValidationFails() throws Exception {
        // Given
        LoginRequest request = new LoginRequest("", "", "", null);

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(), any());
    }

    @Test
    @DisplayName("로그아웃 요청을 하면 200 OK를 반환한다")
    void logout_shouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(post("/auth/logout")).andExpect(status().isOk());

        verify(authService, times(1)).logout();
    }
}
