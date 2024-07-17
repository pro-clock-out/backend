package com.hexcode.pro_clock_out.calendar.service;

import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import com.hexcode.pro_clock_out.calendar.dto.FindCalendarDetailResponse;
import com.hexcode.pro_clock_out.calendar.dto.FindWeeklyCalendarResponse;
import com.hexcode.pro_clock_out.calendar.repository.CalendarRepository;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {
    private final MemberService memberService;
    private final CalendarRepository calendarRepository;

    public Calendar findCalendarById(final Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new MemberNotFoundException(calendarId));
    }

//    public FindWeeklyCalendarResponse findWeeklyCalendar(Long memberId, String option) {
//        long year
//    }



    public FindCalendarDetailResponse findCalendarDetail(Long calendarId) {
        Calendar calendar = findCalendarById(calendarId);
        return FindCalendarDetailResponse.createWith(calendar);
    }



}
