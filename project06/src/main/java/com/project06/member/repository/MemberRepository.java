package com.project06.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project06.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserid(String userid);
}
