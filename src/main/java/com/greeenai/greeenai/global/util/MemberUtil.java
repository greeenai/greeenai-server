package com.greeenai.greeenai.global.util;

import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.domain.member.repository.MemberRepository;
import com.greeenai.greeenai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.greeenai.greeenai.global.error.exception.ErrorCode.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberRepository memberRepository;

    // 현재 로그인한 Member를 조회합니다.
    public Member getCurrentMember() {
        return memberRepository.findById(getCurrentMemberId())
                .orElseThrow(() -> new CustomException(UNAUTHORIZED));
    }

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        validateAuthenticationNotNull(authentication);

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new CustomException(UNAUTHORIZED);
        }
    }

    private void validateAuthenticationNotNull(Authentication authentication) {
        if (authentication == null) {
            throw new CustomException(UNAUTHORIZED);
        }
    }
}
