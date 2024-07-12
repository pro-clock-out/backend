package com.hexcode.pro_clock_out.daily.repository;

import com.hexcode.pro_clock_out.daily.domain.Daily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<Daily, Long> {
}
