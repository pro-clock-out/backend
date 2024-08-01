package com.hexcode.pro_clock_out.calendar.repository;

import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findCalendarsById(Long calendarId);
    List<Calendar> findAllByMemberId(Long memberId);
}
