package com.greeenai.greeenai.domain.member.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.greeenai.greeenai.domain.member.service.MemberService;
import com.greeenai.greeenai.global.config.SecurityConfig;
import com.greeenai.greeenai.global.security.JwtService;
import com.greeenai.greeenai.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class WithdrawMemberTests {

        @Test
        @DisplayName("회원 탈퇴 요청을 하면 200 OK를 반환한다")
        void withdrawMember_shouldReturnOk() throws Exception {
            // When & Then
            mockMvc.perform(delete("/members/me").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(memberService, times(1)).withdrawMember();
        }
    }
}
