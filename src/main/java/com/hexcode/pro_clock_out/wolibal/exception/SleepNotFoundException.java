package com.hexcode.pro_clock_out.wolibal.exception;

import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;

public class SleepNotFoundException extends RuntimeException {

    public SleepNotFoundException() { super("Sleep을 찾을 수 없습니다.");}

    public SleepNotFoundException(Long sleepId) {
        super("Sleep: " + sleepId + "를 찾을 수 없습니다.");
    }

    public SleepNotFoundException(Wolibal wolibal) {
        super("Wolibal: " + wolibal.getId() + "의 Sleep을 찾을 수 없습니다.");
    }
}