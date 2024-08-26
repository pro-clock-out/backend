package com.hexcode.pro_clock_out.member.exception;

import java.time.LocalDate;

public class WolibalNotFoundException extends RuntimeException {
    public WolibalNotFoundException() {
        super("Wolibal을 찾을 수 없습니다.");
    }

    public WolibalNotFoundException(Long memberId) {
        super("Member: " + memberId + "의 워라밸을 찾을 수 없습니다.");
    }

    public WolibalNotFoundException(LocalDate date, Long memberId) {
        super("Member:" + memberId + " 의 Date:" + date + " 워라밸을 찾을 수 없습니다.");
    }
}
