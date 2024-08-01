package com.hexcode.pro_clock_out.calendar.repository;

import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
