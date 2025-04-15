package com.greeenai.greeenai.domain.member.service;

import static com.greeenai.greeenai.global.common.constant.TestConstants.*;
import static org.mockito.Mockito.*;

import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.domain.member.repository.MemberRepository;
import com.greeenai.greeenai.global.util.MemberUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test"})
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberUtil memberUtil;

    @InjectMocks
    private MemberService memberService;

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class WithdrawMemberTests {
        @Test
        @DisplayName("회원 탈퇴 요청을 하면 회원을 삭제한다")
        void withdrawMember_shouldDeleteCurrentMember() {
            // Given
            Member currentMember = Member.create(TEST_NAME, TEST_EMAIL, TEST_OAUTH_ID, TEST_OAUTH_PROVIDER);
            when(memberUtil.getCurrentMember()).thenReturn(currentMember);

            // When
            memberService.withdrawMember();

            // Then
            verify(memberRepository).delete(currentMember);
        }
    }
}
