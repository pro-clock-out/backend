package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.domain.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Optional<Work> findByWolibal(Wolibal wolibal);
}
