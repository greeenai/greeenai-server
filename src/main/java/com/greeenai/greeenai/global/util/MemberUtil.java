package com.greeenai.greeenai.global.util;

import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberRepository memberRepository;

    // 현재 로그인한 Member를 조회합니다.
    public Member getCurrentMember() {
        // todo: throw CustomException
        return memberRepository.findById(getCurrentMemberId()).orElseThrow(() -> new IllegalArgumentException());
    }

    public Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        validateAuthenticationNotNull(authentication);

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            // todo: throw CustomException
            throw new IllegalArgumentException();
        }
    }

    private void validateAuthenticationNotNull(Authentication authentication) {
        if (authentication == null) {
            // todo: throw CustomException
        }
    }
}
