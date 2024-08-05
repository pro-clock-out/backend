package com.hexcode.pro_clock_out.wolibal.exception;

public class RestNotFoundException extends RuntimeException {

    public RestNotFoundException() { super("Rest를 찾을 수 없습니다");}

    public RestNotFoundException(Long restId) { super("Rest: " + restId + "를 찾을 수 없습니다.");}

}
