package com.hexcode.pro_clock_out.daily.repository;

import com.hexcode.pro_clock_out.daily.domain.DailyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyGoalRepository extends JpaRepository<DailyGoal, Long> {
    List<DailyGoal> findByDailyId(Long dailyId);
}
