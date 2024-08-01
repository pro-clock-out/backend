package com.hexcode.pro_clock_out.daily.repository;

import com.hexcode.pro_clock_out.daily.domain.Goal;
import com.hexcode.pro_clock_out.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findGoalByMember(Member member);
}
