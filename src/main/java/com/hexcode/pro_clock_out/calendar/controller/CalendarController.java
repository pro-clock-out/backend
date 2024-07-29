package com.hexcode.pro_clock_out.calendar.controller;

import com.hexcode.pro_clock_out.auth.dto.CustomUserDetails;
import com.hexcode.pro_clock_out.calendar.dto.*;
import com.hexcode.pro_clock_out.calendar.service.CalendarService;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.calendar.dto.AddCalendarResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendars/{calendarId}")
    public ResponseEntity<ResponseDto> getCalendarEventDetails(Authentication authentication, @PathVariable("calendarId") Long calendarId) {
        log.info("Request to get Calendar Event Details");
        FindCalendarDetailResponse response = calendarService.findCalendarDetail(calendarId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/calendars")
    public ResponseEntity<ResponseDto> addCalendarEvent(
            Authentication authentication,
            @RequestBody UpdateCalendarRequest request){
        log.info("Request to add Event");
        Long memberId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        AddCalendarResponse response = calendarService.addCalendar(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/calendars/{calendarId}")
    public ResponseEntity<ResponseDto> updateCalendarEvent(Authentication authentication, @PathVariable("calendarId") Long calendarId, @RequestBody UpdateCalendarRequest request){
        log.info("Request to add Event");
        UpdateCalendarResponse response = calendarService.updateCalendar(calendarId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/calendars/{calendarId}")
    public ResponseEntity<?> deleteCalendarEvent(Authentication authentication, @PathVariable("calendarId") Long calendarId){
        log.info("Request to delete Calendar Event Details");
        try {
            calendarService.deleteCalendar(calendarId);
            return ResponseEntity.ok("삭제 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/calendars/{memberId}")
    public ResponseEntity<?> getAllCalendarEvents(Authentication authentication, @RequestParam("memberId") Long memberId) {
        try {
            List<CalendarEventResponse> calendars = calendarService.getAllCalendarEvents(memberId);
            return ResponseEntity.ok(calendars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
