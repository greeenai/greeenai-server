package com.greeenai.greeenai.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.greeenai.greeenai.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByAuthId(String authId);
}
