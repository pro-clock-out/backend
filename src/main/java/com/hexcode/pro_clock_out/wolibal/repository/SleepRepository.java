package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.wolibal.domain.Sleep;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
    Optional<Sleep> findByWolibal(Wolibal wolibal);
}
