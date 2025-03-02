package com.greeenai.greeenai.domain.member.controller;

import com.greeenai.greeenai.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdrawMember() {
        memberService.withdrawMember();
        return ResponseEntity.ok().build();
    }
}
