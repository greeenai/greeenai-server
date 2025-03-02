package com.greeenai.greeenai.domain.member.service;

import com.greeenai.greeenai.domain.member.domain.Member;
import com.greeenai.greeenai.domain.member.repository.MemberRepository;
import com.greeenai.greeenai.global.util.MemberUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberUtil memberUtil;
    private final MemberRepository memberRepository;

    @Transactional
    public void withdrawMember() {
        Member currentMember = memberUtil.getCurrentMember();

        memberRepository.delete(currentMember);

        log.info("[MemberService] 회원 탈퇴 성공 : memberId={}", currentMember.getId());
    }
}
