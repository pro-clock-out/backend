package com.hexcode.pro_clock_out.calendar.exception;

public class CalendarNotFoundException extends RuntimeException {
    public CalendarNotFoundException() {
        super("Calendar를 찾을 수 없습니다.");
    }

    public CalendarNotFoundException(Long calendarId) {
        super("Calendar : " + calendarId + " 를 찾을 수 없습니다.");
    }
}
