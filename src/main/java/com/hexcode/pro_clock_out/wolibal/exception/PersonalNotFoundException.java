package com.hexcode.pro_clock_out.wolibal.exception;

import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;

public class PersonalNotFoundException extends RuntimeException {

    public PersonalNotFoundException() { super("Personal을 찾을 수 없습니다.");}

    public PersonalNotFoundException(Long personalId) { super("Personal: " + personalId + "를 찾을 수 없습니다.");
    }

    public PersonalNotFoundException(Wolibal wolibal) {
        super("Wolibal: " + wolibal.getId() + "의 Personal를 찾을 수 없습니다.");
    }
}
