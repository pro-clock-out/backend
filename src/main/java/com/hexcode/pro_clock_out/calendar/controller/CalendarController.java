package com.hexcode.pro_clock_out.calendar.controller;

import com.hexcode.pro_clock_out.calendar.dto.FindCalendarDetailResponse;
import com.hexcode.pro_clock_out.calendar.dto.FindWeeklyCalendarResponse;
import com.hexcode.pro_clock_out.calendar.service.CalendarService;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.wolibal.dto.FindTotalWolibalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CalendarController {

    private final CalendarService calendarService;

//    @GetMapping("calendars")
//    public ResponseEntity<ResponseDto> getWeeklyCalendar(Authentication authentication, @RequestParam("option") String option) {
//        log.info("Request to get Weekly Calendar");
//        Long memberId = (Long) authentication.getPrincipal();
//        FindWeeklyCalendarResponse response = calendarService.findWeeklyCalendar(memberId, option);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    @GetMapping("/calendars/{calendarId}")
    public ResponseEntity<ResponseDto> getCalendarEventDetails(Authentication authentication, @RequestParam("calendarId") Long calendarId) {
        log.info("Request to get Calendar Event Details");
        FindCalendarDetailResponse response = calendarService.findCalendarDetail(calendarId);
        return new ResponseEntity<>(response, HttpStatus.OK);


    }

}
