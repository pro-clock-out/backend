package com.hexcode.pro_clock_out.member.repository;

import com.hexcode.pro_clock_out.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existByEmail(String email);
    boolean existByNickname(String nickname);
    Optional<Member> findByEmail(String email);
}
