package com.hexcode.pro_clock_out.wolibal.exception;

import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;

public class RestNotFoundException extends RuntimeException {

    public RestNotFoundException() { super("Rest를 찾을 수 없습니다");}

    public RestNotFoundException(Long restId) { super("Rest: " + restId + "를 찾을 수 없습니다.");}

    public RestNotFoundException(Wolibal wolibal) {
        super("Wolibal: " + wolibal.getId() + "의 Rest를 찾을 수 없습니다.");
    }
}
