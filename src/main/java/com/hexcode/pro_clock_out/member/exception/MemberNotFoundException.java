package com.hexcode.pro_clock_out.member.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("Member를 찾을 수 없습니다.");
    }

    public MemberNotFoundException(Long memberId) {
        super("Member: " + memberId + " 를 찾을 수 없습니다.");
    }
}
