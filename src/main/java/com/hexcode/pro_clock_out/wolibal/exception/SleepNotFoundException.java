package com.hexcode.pro_clock_out.wolibal.exception;

import com.hexcode.pro_clock_out.wolibal.domain.Sleep;

public class SleepNotFoundException extends RuntimeException {

    public SleepNotFoundException() { super("Sleep을 찾을 수 없습니다.");}

    public SleepNotFoundException(Long sleepId) {
        super("Sleep: " + sleepId + "를 찾을 수 없습니다.");
    }
}