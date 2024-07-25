package com.hexcode.pro_clock_out.daily.exception;

public class DailyNotFoundException extends RuntimeException {

    public DailyNotFoundException() {
        super("Daily를 찾을 수 없습니다.");
    }

    public DailyNotFoundException(Long dailyId) {
        super("Daily: " + dailyId + " 를 찾을 수 없습니다.");
    }
}

